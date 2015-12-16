package com.github.fge.fs.api.entity;

import com.github.fge.fs.api.entity.FileSystemEntity;

import java.io.IOException;
import java.nio.file.Path;

public interface FileSystemEntityProvider
{
    FileSystemEntity getEntity(Path path)
        throws IOException;
}
