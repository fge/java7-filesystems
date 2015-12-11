package com.github.fge.fs.api.path;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

final class PathIterator
    implements Iterator<Path>
{
    private final Path path;
    private final int nameCount;
    @SuppressWarnings("RedundantFieldInitialization")
    private int index = 0;

    PathIterator(final Path path)
    {
        this.path = path;
        nameCount = path.getNameCount();
    }

    @Override
    public boolean hasNext()
    {
        return index < nameCount;
    }

    @Override
    public Path next()
    {
        if (hasNext())
            return path.getName(index++);

        throw new NoSuchElementException();

    }
}
