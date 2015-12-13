package com.github.fge.fs.api.path.elements;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public abstract class AbstractPathElementsFactory
    implements PathElementsFactory
{
    protected static final String[] NO_ELEMENTS = new String[0];

    protected final BiFunction<String, String[], PathElements> builder;

    protected AbstractPathElementsFactory(
        final BiFunction<String, String[], PathElements> builder)
    {
        this.builder = builder;
    }

    protected abstract boolean isValidName(String name);

    @Override
    public final PathElements getRoot(final PathElements elements)
    {
        final String root = elements.getRoot();
        return root == null ? null : builder.apply(root, NO_ELEMENTS);
    }

    @Override
    public final PathElements getFileName(final PathElements elements)
    {
        final String[] names = elements.getNames();
        final int len = names.length;
        if (len == 0)
            return null;
        final String[] newNames = { names[len - 1] };
        return builder.apply(null, newNames);
    }

    @Override
    public final PathElements getParent(final PathElements elements)
    {
        final String root = elements.getRoot();
        final String[] names = elements.getNames();
        final int len = names.length;
        if (len == 0)
            return null;
        if (len == 1 && root == null)
            return null;
        final String[] newNames = Arrays.copyOf(names, len - 1);
        return builder.apply(root, newNames);
    }

    @Override
    public final PathElements getName(final PathElements elements,
        final int index)
    {
        final String[] newNames = { elements.getNames()[index] };
        return builder.apply(null, newNames);
    }

    @Override
    public final PathElements getNames(final PathElements elements,
        final int beginIndex, final int endIndex)
    {
        final String[] newNames = Arrays.copyOfRange(elements.getNames(),
            beginIndex, endIndex);
        if (newNames.length == 0)
            throw new IllegalArgumentException();
        return builder.apply(null, newNames);
    }

    protected abstract boolean rootStartsWith(String root, String otherRoot);

    @Override
    public boolean startsWith(final PathElements elements,
        final PathElements otherElements)
    {
        // Shortcuts: no dice if roots are different, or other has more names
        if (!rootStartsWith(elements.getRoot(), otherElements.getRoot()))
            return false;
        final String[] names = elements.getNames();
        final String[] otherNames = otherElements.getNames();
        final int length = otherNames.length;
        return length <= names.length && IntStream.range(0, length)
            .allMatch(index -> names[index].equals(otherNames[index]));
    }

    protected abstract boolean rootEndsWith(String root, String otherRoot);

    @Override
    public boolean endsWith(final PathElements elements,
        final PathElements otherElements)
    {
        final String root = elements.getRoot();
        final String otherRoot = otherElements.getRoot();
        if (otherRoot != null)
            return root != null
                && rootEndsWith(root, otherRoot)
                && Arrays.equals(elements.getNames(), otherElements.getNames());
        final String[] otherNames = otherElements.getNames();
        final int length = otherNames.length;
        final String[] names = elements.getNames();
        final int namesIndex = names.length - length;
        return namesIndex >= 0 && IntStream.range(0, length).allMatch(
            index -> otherNames[index].equals(names[index + namesIndex])
        );
    }

    @Override
    public final PathElements resolve(final PathElements elements,
        final PathElements otherElements)
    {
        if (otherElements.isAbsolute() || elements.isEmptyElements())
            return otherElements;
        if (otherElements.isEmptyElements())
            return elements;
        if (otherElements.getRoot() != null)
            return resolveOtherRootNonAbsolute(elements, otherElements);
        final String root = elements.getRoot();
        final String[] names = elements.getNames();
        final String[] otherNames = otherElements.getNames();
        final int namesLength = names.length;
        final int otherNamesLength = otherNames.length;
        final String[] newNames = Arrays.copyOf(names,
            namesLength + otherNamesLength);
        System.arraycopy(otherNames, 0, newNames, namesLength,
            otherNamesLength);
        return builder.apply(root, newNames);
    }

    protected abstract PathElements resolveOtherRootNonAbsolute(
        PathElements elements, PathElements otherElements);

    @Override
    public PathElements relativize(final PathElements elements,
        final PathElements otherElements)
    {
        final String firstRoot = elements.getRoot();
        final boolean firstHasRoot = firstRoot != null;
        final String otherRoot = otherElements.getRoot();
        final boolean otherHasRoot = otherRoot != null;
        /*
         * If only one of them has a root then this is an illegal situation
         */
        if (firstHasRoot ^ otherHasRoot)
            throw new IllegalArgumentException();
        /*
         * Here we know that if the first has a root, the second does not and
         * vice versa.
         *
         * Which means that if the first has a root, both have.
         *
         * Otherwise it means none have: this simplifies matters, since we know
         * the root is null in both cases. In this case we delegate to a method
         * which relativizes only the names components.
         */
        if (firstHasRoot)
            return relativizeBothRoots(elements, otherElements);

        final String[] names = elements.getNames();
        final String[] otherNames = otherElements.getNames();
        return builder.apply(null, relativizeNames(names, otherNames));
    }

    protected abstract String[] relativizeNames(final String[] names,
        final String[] otherNames);

    protected abstract PathElements relativizeBothRoots(PathElements elements,
        PathElements otherElements);
}
