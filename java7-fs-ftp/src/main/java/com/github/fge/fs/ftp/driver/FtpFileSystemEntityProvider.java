package com.github.fge.fs.ftp.driver;

import com.github.fge.fs.api.driver.FileSystemEntity;
import com.github.fge.fs.api.driver.FileSystemEntityProvider;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
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
            return new FtpFileSystemEntity(ftpFile);
        final int reply = ftpClient.getReply();
        throw FTPReply.isPositiveCompletion(reply)
            ? new NoSuchFileException(pathname)
            : new IOException("FTP protocol error; reply code = " + reply);
    }
}