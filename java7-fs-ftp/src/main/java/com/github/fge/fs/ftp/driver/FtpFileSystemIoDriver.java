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
    private final FTPClient client;

    public FtpFileSystemIoDriver(final FTPClient client)
    {
        this.client = client;
    }

    @Override
    public InputStream getInputStream(final Path path,
        final Set<OpenOption> options)
        throws IOException
    {
        // TODO: check
        final String name = path.toAbsolutePath().toString();
        final InputStream stream = client.retrieveFileStream(name);
        if (stream == null)
            throw new IOException("FTP error :" + client.getReplyCode());
        return new FtpInputStream(client, stream);
    }

    @Override
    public DirectoryStream<Path> getDirectoryStream(final Path dir,
        final DirectoryStream.Filter<? super Path> filter)
        throws IOException
    {
        // TODO: check
        final String name = dir.toAbsolutePath().toString();
        final FTPListParseEngine engine = client.initiateListParsing(name);
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
