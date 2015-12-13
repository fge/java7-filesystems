package com.github.fge.fs.api.path.elements;

import java.nio.file.InvalidPathException;
import java.util.Objects;
import java.util.regex.Pattern;

public class UnixPathElementsFactory
    extends HierarchicalPathElementsFactory
{
    private static final Pattern LEADING_SLASHES = Pattern.compile("^/+");
    private static final Pattern SLASHES = Pattern.compile("/+");
    private static final Pattern TRAILING_SLASHES = Pattern.compile("/+$");

    private static final PathElements EMPTY
        = new UnixPathElements(null, new String[] { "" });
    private static final PathElements ROOT
        = new UnixPathElements("/", NO_ELEMENTS);

    public UnixPathElementsFactory()
    {
        super(UnixPathElements::new);
    }

    @Override
    protected boolean isValidName(final String name)
    {
        return !name.isEmpty()
            && name.codePoints().allMatch(i -> i != 0 && i != '/');
    }

    @Override
    public final PathElements buildElements(final String input)
    {
        String tmp = LEADING_SLASHES.matcher(input).replaceFirst("");

        final String root = tmp.equals(input) ? null : "/";

        tmp = TRAILING_SLASHES.matcher(tmp).replaceFirst("");

        if (tmp.isEmpty())
            return root != null ? ROOT : EMPTY;

        final String[] names = SLASHES.split(tmp);

        for (final String name: names)
            if (!isValidName(name))
                throw new InvalidPathException(input, "invalid name element '"
                    + name + '\'');

        return builder.apply(root, names);
    }

    @Override
    public PathElements getFileSystemRoot()
    {
        return ROOT;
    }

    @Override
    protected boolean rootStartsWith(final String root, final String otherRoot)
    {
        return Objects.equals(root, otherRoot);
    }

    @Override
    protected boolean rootEndsWith(final String root, final String otherRoot)
    {
        return Objects.equals(root, otherRoot);
    }

    @Override
    protected PathElements resolveOtherRootNonAbsolute(
        final PathElements elements, final PathElements otherElements)
    {
        // cannot happen: a path is absolute iif it has a root and vice versa
        throw new IllegalStateException();
    }

    @Override
    protected PathElements relativizeBothRoots(final PathElements elements,
        final PathElements otherElements)
    {
        final String[] names = relativizeNames(elements.getNames(),
            otherElements.getNames());
        return names.length == 0 ? EMPTY : builder.apply(null, names);
    }
}
