package com.github.fge.fs.ftp.driver;

import com.github.fge.fs.api.directory.DefaultDirectoryStream;
import com.github.fge.fs.api.driver.ReadOnlyFileSystemIoDriver;
import com.github.fge.fs.ftp.directory.FtpDirectorySpliterator;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPListParseEngine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessMode;
import java.nio.file.DirectoryStream;
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
        this.ftpClient = ftpClient;
    }

    @Override
    public InputStream getInputStream(final Path path,
        final Set<OpenOption> options)
        throws IOException
    {
        // TODO: check
        final String name = path.toAbsolutePath().toString();
        return ftpClient.retrieveFileStream(name);
    }

    @Override
    public DirectoryStream<Path> getDirectoryStream(final Path dir,
        final DirectoryStream.Filter<? super Path> filter)
        throws IOException
    {
        // TODO: check
        final String name = dir.toAbsolutePath().toString();
        final FTPListParseEngine engine = ftpClient.initiateListParsing(name);
        final Spliterator<Path> spliterator
            = new FtpDirectorySpliterator(dir, engine);
        final Stream<Path> stream = StreamSupport.stream(spliterator, true);
        return new DefaultDirectoryStream(stream, filter);
    }

    @Override
    public void checkAccess(final Path path, final AccessMode... modes)
        throws IOException
    {
        // TODO
    }
}
