package com.github.fge.fs.ftp.driver;

import com.github.fge.fs.api.driver.FileSystemEntity;
import org.apache.commons.net.ftp.FTPFile;

import java.nio.file.AccessMode;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public final class FtpFileSystemEntity
    extends FileSystemEntity
{
    private static final int[] FTP_ACCESS_TYPE = {
        FTPFile.USER_ACCESS, FTPFile.GROUP_ACCESS, FTPFile.WORLD_ACCESS
    };

    private static final Map<AccessMode, Integer> PERMISSIONS_MAP
        = new EnumMap<>(AccessMode.class);

    static {
        PERMISSIONS_MAP.put(AccessMode.READ, FTPFile.READ_PERMISSION);
        PERMISSIONS_MAP.put(AccessMode.WRITE, FTPFile.WRITE_PERMISSION);
        PERMISSIONS_MAP.put(AccessMode.EXECUTE, FTPFile.EXECUTE_PERMISSION);
    }

    private final FTPFile ftpFile;

    public FtpFileSystemEntity(final Path path, final FTPFile file)
    {
        super(path);
        ftpFile = file;
    }

    @Override
    public Type getType()
    {
        if (ftpFile.isFile())
            return Type.REGULAR_FILE;
        if (ftpFile.isDirectory())
            return Type.DIRECTORY;
        // TODO: because no symlink support...
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasAccess(final AccessMode... modes)
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
