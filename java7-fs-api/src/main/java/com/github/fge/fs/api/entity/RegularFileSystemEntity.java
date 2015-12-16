package com.github.fge.fs.api.entity;

public interface RegularFileSystemEntity
    extends FileSystemEntity
{
    @Override
    default EntityType getType()
    {
        return EntityType.REGULAR_FILE;
    }
}
