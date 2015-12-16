package com.github.fge.fs.ftp.entity;

import com.github.fge.fs.api.entity.RegularFileSystemEntity;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.Path;

public final class FtpRegularFileSystemEntity
    extends FtpFileSystemEntity
    implements RegularFileSystemEntity
{
    public FtpRegularFileSystemEntity(final Path path,
        final FTPClient ftpClient, final FTPFile ftpFile)
    {
        super(path, ftpClient, ftpFile);
    }

    @Override
    public InputStream getInputStream()
        throws IOException
    {
        if (!hasAccess(AccessMode.READ))
            throw new AccessDeniedException(name);

        return ftpClient.retrieveFileStream(name);
    }
}
