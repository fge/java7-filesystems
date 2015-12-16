package com.github.fge.fs.ftp.driver;

import com.github.fge.fs.api.driver.FileSystemEntity;
import com.github.fge.fs.api.driver.ReadOnlyFileSystemIoDriver;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Set;

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
        final String name = path.toAbsolutePath().toString();

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
}
