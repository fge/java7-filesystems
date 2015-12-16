package com.github.fge.fs.ftp.driver;

import com.github.fge.fs.api.directory.DefaultDirectoryStream;
import com.github.fge.fs.api.entity.FileSystemEntity;
import com.github.fge.fs.api.driver.ReadOnlyFileSystemIoDriver;
import com.github.fge.fs.ftp.directory.FtpDirectorySpliterator;
import com.github.fge.fs.ftp.entity.FtpFileSystemEntityProvider;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPListParseEngine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class FtpFileSystemIoDriver
    extends ReadOnlyFileSystemIoDriver
{
    private final FTPClient ftpClient;

    public FtpFileSystemIoDriver(final FTPClient ftpClient)
    {
        super(new FtpFileSystemEntityProvider(ftpClient));
        this.ftpClient = ftpClient;
    }

    @Override
    public InputStream getInputStream(final Path path,
        final Set<OpenOption> options)
        throws IOException
    {
        final FileSystemEntity entity = entityProvider.getEntity(path);
        final String name = entity.toString();

        switch (entity.getType()) {
            case ENOENT:
                throw new NoSuchFileException(name);
            case REGULAR_FILE:
                break;
            default:
                throw new IOException(name + " is not a regular file");
        }

        return ftpClient.retrieveFileStream(name);
    }

    @Override
    public DirectoryStream<Path> getDirectoryStream(final Path dir,
        final DirectoryStream.Filter<? super Path> filter)
        throws IOException
    {
        final FileSystemEntity entity = entityProvider.getEntity(dir);
        final String name = entity.toString();

        switch (entity.getType()) {
            case ENOENT:
                throw new NoSuchFileException(name);
            case DIRECTORY:
                break;
            default:
                throw new NotDirectoryException(name);
        }

        final FTPListParseEngine engine = ftpClient.initiateListParsing(name);
        final Spliterator<Path> spliterator
            = new FtpDirectorySpliterator(dir, engine);
        final Stream<Path> stream = StreamSupport.stream(spliterator, true);
        return new DefaultDirectoryStream(stream, filter);
    }
}
