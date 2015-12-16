package com.github.fge.fs.api.entity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessMode;

public interface FileSystemEntity
{
    EntityType getType();

    boolean hasAccess(AccessMode... modes);

    InputStream getInputStream()
        throws IOException;

    @Override
    String toString();
}
