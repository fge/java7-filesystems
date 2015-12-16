package com.github.fge.fs.ftp.entity;

import com.github.fge.fs.api.entity.DirectoryFileSystemEntity;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.nio.file.Path;

public final class FtpDirectoryFileSystemEntity
    extends FtpFileSystemEntity
    implements DirectoryFileSystemEntity
{
    public FtpDirectoryFileSystemEntity(final Path path,
        final FTPClient ftpClient, final FTPFile ftpFile)
    {
        super(path, ftpClient, ftpFile);
    }
}
