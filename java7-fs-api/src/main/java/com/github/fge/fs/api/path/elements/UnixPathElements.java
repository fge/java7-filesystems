package com.github.fge.fs.api.path.elements;

import java.util.Arrays;

public final class UnixPathElements
    extends PathElements
{
    public UnixPathElements(final String root, final String[] names)
    {
        super(root, "/", names);
    }

    @Override
    public boolean isAbsolute()
    {
        return root != null;
    }

    @Override
    protected int namesHashCode()
    {
        return Arrays.hashCode(names);
    }

    @Override
    protected boolean namesEquals(final String[] otherNames)
    {
        return Arrays.equals(names, otherNames);
    }
}
