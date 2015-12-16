package com.github.fge.fs.api.entity;

import java.nio.file.AccessMode;
import java.nio.file.Path;

public abstract class FileSystemEntity
{
    protected final String name;

    protected FileSystemEntity(final Path path)
    {
        name = path.toAbsolutePath().toString();
    }

    public abstract Type getType();

    public abstract boolean hasAccess(AccessMode... modes);

    public enum Type {
        REGULAR_FILE,
        DIRECTORY,
        SYMLINK,
        OTHER,
        ENOENT,
        ;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
