package com.github.fge.fs.api.path.elements;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class PathElements
{
    protected final String root;
    protected final String nameSeparator;
    protected final String[] names;

    protected PathElements(final String root, final String nameSeparator,
        final String[] names)
    {
        this.root = root;
        this.nameSeparator = nameSeparator;
        this.names = names;
    }

    public final String getRoot()
    {
        return root;
    }

    public abstract boolean isAbsolute();

    public final String[] getNames()
    {
        return names;
    }

    public final boolean isEmptyElements()
    {
        return root == null && names.length == 1 && names[0].isEmpty();
    }

    @Override
    public final int hashCode()
    {
        return 31 * Objects.hash(root, nameSeparator) + namesHashCode();
    }

    protected abstract int namesHashCode();

    @Override
    public final boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (!(obj instanceof PathElements))
            return false;
        final PathElements other = (PathElements) obj;
        return Objects.equals(root, other.root)
            && nameSeparator.equals(other.nameSeparator)
            && namesEquals(other.names);
    }

    protected abstract boolean namesEquals(String[] otherNames);

    @Override
    public final String toString()
    {
        final String elements = Arrays.stream(names)
            .collect(Collectors.joining(nameSeparator));

        return root == null ? elements : root + elements;
    }
}
