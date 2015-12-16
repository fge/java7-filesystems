package com.github.fge.fs.ftp.driver;

import com.github.fge.fs.api.driver.NoSuchFileSystemEntity;
import com.github.fge.fs.api.driver.FileSystemEntity;
import com.github.fge.fs.api.driver.FileSystemEntityProvider;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.nio.file.Path;

public final class FtpFileSystemEntityProvider
    implements FileSystemEntityProvider
{
    private final FTPClient ftpClient;

    public FtpFileSystemEntityProvider(final FTPClient ftpClient)
    {
        this.ftpClient = ftpClient;
    }

    @Override
    public FileSystemEntity getEntity(final Path path)
        throws IOException
    {
        final String pathname = path.toAbsolutePath().toString();
        final FTPFile ftpFile = ftpClient.mlistFile(pathname);
        if (ftpFile != null)
            return new FtpFileSystemEntity(path, ftpFile);
        final int reply = ftpClient.getReply();
        if (FTPReply.isPositiveCompletion(reply))
            return NoSuchFileSystemEntity.forPath(path);
        throw new IOException("FTP protocol error; reply code = " + reply);
    }
}
