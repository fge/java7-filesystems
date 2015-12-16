package com.github.fge.fs.api.entity;

import java.nio.file.Path;

public abstract class AbstractFileSystemEntity
    implements FileSystemEntity
{
    protected final String name;

    protected AbstractFileSystemEntity(final Path path)
    {
        name = path.toAbsolutePath().toString();
    }

    @Override
    public String toString()
    {
        return name;
    }
}
