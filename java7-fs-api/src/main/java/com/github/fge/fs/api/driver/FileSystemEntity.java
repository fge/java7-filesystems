package com.github.fge.fs.api.driver;

import java.nio.file.AccessMode;

public interface FileSystemEntity
{
    Type getType();

    boolean hasAccess(AccessMode... modes);

    enum Type {
        REGULAR_FILE,
        DIRECTORY,
        SYMLINK,
        OTHER,
        ENOENT,
        ;
    }
}
