package com.github.fge.fs.ftp.entity;

import com.github.fge.fs.api.entity.AbstractFileSystemEntity;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.nio.file.AccessMode;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public abstract class FtpFileSystemEntity
    extends AbstractFileSystemEntity
{
    protected static final int[] FTP_ACCESS_TYPE = {
        FTPFile.USER_ACCESS, FTPFile.GROUP_ACCESS, FTPFile.WORLD_ACCESS
    };

    protected static final Map<AccessMode, Integer> PERMISSIONS_MAP
        = new EnumMap<>(AccessMode.class);

    static {
        PERMISSIONS_MAP.put(AccessMode.READ, FTPFile.READ_PERMISSION);
        PERMISSIONS_MAP.put(AccessMode.WRITE, FTPFile.WRITE_PERMISSION);
        PERMISSIONS_MAP.put(AccessMode.EXECUTE, FTPFile.EXECUTE_PERMISSION);
    }

    protected final FTPClient ftpClient;
    protected final FTPFile ftpFile;

    protected FtpFileSystemEntity(final Path path, final FTPClient ftpClient,
        final FTPFile ftpFile)
    {
        super(path);
        this.ftpClient = ftpClient;
        this.ftpFile = ftpFile;
    }

    @Override
    public final boolean hasAccess(final AccessMode... modes)
    {
        int permission;

        for (final AccessMode mode: modes) {
            permission = PERMISSIONS_MAP.get(mode);
            for (final int access: FTP_ACCESS_TYPE)
                if (ftpFile.hasPermission(access, permission))
                    return true;
        }

        return false;
    }
}
