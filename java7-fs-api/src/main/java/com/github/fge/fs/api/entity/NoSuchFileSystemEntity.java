package com.github.fge.fs.api.entity;

import java.io.InputStream;
import java.nio.file.AccessMode;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public final class NoSuchFileSystemEntity
    extends AbstractFileSystemEntity
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
    public EntityType getType()
    {
        return EntityType.ENOENT;
    }

    @Override
    public boolean hasAccess(final AccessMode... modes)
    {
        throw new IllegalStateException();
    }

    @Override
    public InputStream getInputStream()
        throws NoSuchFileException
    {
        throw new NoSuchFileException(name);
    }
}
