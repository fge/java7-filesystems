package com.github.fge.fs.api.path;

import com.github.fge.fs.api.fs.AbstractFileSystem;
import com.github.fge.fs.api.path.elements.PathElements;
import com.github.fge.fs.api.path.elements.PathElementsFactory;

import java.nio.file.Path;

public abstract class PathEngine
{
    protected final PathElementsFactory pathElementsFactory;

    protected PathEngine(final PathElementsFactory factory)
    {
        pathElementsFactory = factory;
    }

    public abstract Path buildPath(AbstractFileSystem fs,
        PathElements elements);

    public PathElementsFactory getPathElementsFactory()
    {
        return pathElementsFactory;
    }
}
