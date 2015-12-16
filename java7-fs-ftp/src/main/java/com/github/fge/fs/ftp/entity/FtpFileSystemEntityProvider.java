package com.github.fge.fs.ftp.entity;

import com.github.fge.fs.api.entity.NoSuchFileSystemEntity;
import com.github.fge.fs.api.entity.FileSystemEntity;
import com.github.fge.fs.api.entity.FileSystemEntityProvider;
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
        final int reply = ftpClient.getReply();
        if (!FTPReply.isPositiveCompletion(reply))
            throw new IOException("FTP protocol error; reply code = " + reply);
        if (ftpFile == null)
            return NoSuchFileSystemEntity.forPath(path);
        return new FtpRegularFileSystemEntity(path, ftpClient, ftpFile);
    }
}
