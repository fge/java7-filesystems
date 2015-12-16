package com.github.fge.fs.api.entity;

import java.nio.file.AccessMode;
import java.nio.file.Path;

public final class NoSuchFileSystemEntity
    extends FileSystemEntity
{
    public static FileSystemEntity forPath(final Path path)
    {
        return new NoSuchFileSystemEntity(path);
    }

    private NoSuchFileSystemEntity(final Path path)
    {
        super(path);
    }

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
