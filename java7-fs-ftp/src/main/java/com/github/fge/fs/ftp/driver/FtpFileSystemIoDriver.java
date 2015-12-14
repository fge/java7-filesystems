package com.github.fge.fs.ftp.driver;

import com.github.fge.fs.api.driver.FileSystemIoDriver;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Set;

public final class FtpFileSystemIoDriver
    implements FileSystemIoDriver
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
        final String name = path.toAbsolutePath().toString();
        return ftpClient.retrieveFileStream(name);
    }
}
