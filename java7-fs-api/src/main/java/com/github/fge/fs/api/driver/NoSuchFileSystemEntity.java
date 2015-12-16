package com.github.fge.fs.api.driver;

import java.nio.file.AccessMode;

public enum NoSuchFileSystemEntity
    implements FileSystemEntity
{
    INSTANCE,
    ;

    @Override
    public Type getType()
    {
        return Type.ENOENT;
    }

    @Override
    public boolean hasAccess(final AccessMode... modes)
    {
        throw new IllegalStateException();
    }
}
