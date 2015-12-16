package com.github.fge.fs.api.entity;

import java.io.IOException;
import java.io.InputStream;

public interface DirectoryFileSystemEntity
    extends FileSystemEntity
{
    @Override
    default EntityType getType()
    {
        return EntityType.DIRECTORY;
    }

    @Override
    default InputStream getInputStream()
        throws IOException
    {
        throw new IOException(toString() + " is a directory");
    }
}
