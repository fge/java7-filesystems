package com.github.fge.fs.api.path;

import com.github.fge.fs.api.fs.AbstractFileSystem;
import com.github.fge.fs.api.path.elements.PathElements;
import com.github.fge.fs.api.path.elements.PathElementsFactory;

import java.nio.file.Path;

public abstract class PathFactory
{
    protected final PathElementsFactory elementsFactory;

    protected PathFactory(final PathElementsFactory elementsFactory)
    {
        this.elementsFactory = elementsFactory;
    }

    public abstract Path buildPath(AbstractFileSystem fs,
        PathElements elements);

    public PathElementsFactory getElementsFactory()
    {
        return elementsFactory;
    }
}
