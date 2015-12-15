package com.github.fge.fs.api.driver;

import java.io.IOException;
import java.nio.file.Path;

public interface FileSystemEntityProvider
{
    FileSystemEntity getEntity(Path path)
        throws IOException;
}
