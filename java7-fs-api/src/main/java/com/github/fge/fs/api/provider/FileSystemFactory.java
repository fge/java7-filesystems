package com.github.fge.fs.api.provider;

import com.github.fge.fs.api.fs.AbstractFileSystem;

import java.net.URI;
import java.util.Map;

public interface FileSystemFactory
{
    String getScheme();

    AbstractFileSystem createFileSystem(AbstractFileSystemProvider provider,
        URI uri, Map<String, ?> env);
}
