package com.github.fge.fs.api.path.elements;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.function.BiFunction;

public abstract class HierarchicalPathElementsFactory
    extends AbstractPathElementsFactory
{
    private static final String PARENT_TOKEN = "..";
    private static final String SELF_TOKEN = ".";

    protected HierarchicalPathElementsFactory(
        final BiFunction<String, String[], PathElements> builder)
    {
        super(builder);
    }

    @Override
    public final PathElements normalize(final PathElements elements)
    {
        final String root = elements.getRoot();
        final String[] origNames = elements.getNames();
        final int origLength = origNames.length;

        int startIndex = 0;

        /*
         * If there is a root, we start by skipping all leading parent tokens;
         * we do not want to copy them
         */
        if (root != null)
            while (origNames[startIndex].equals(PARENT_TOKEN))
                startIndex++;

        /*
         * Create our list of new names; its predicted length will be that of
         * the original name array length minus the start index.
         *
         * We use a Deque here since we want to be able to remove the last
         * element from the queue easily.
         */
        final Deque<String> deque = new ArrayDeque<>(origLength - startIndex);

        /*
         * Walk the original names array from the start index up to its original
         * length
         */
        String name;
        boolean seenRegularName = false;

        for (int index = startIndex; index < origLength; index++) {
            name = origNames[index];
            switch (name) {
                case SELF_TOKEN:
                    /*
                     * If we see a self token, don't do anything
                     */
                    break;
                case PARENT_TOKEN:
                    /*
                     * If we have not seen a regular name token yet, insert
                     * the token.
                     *
                     * If we have, we remove the last token except if nextIndex
                     * is 0.
                     */
                    if (!seenRegularName) {
                        deque.add(name);
                    } else if (!deque.isEmpty()) {
                        deque.removeLast();
                    }
                    break;
                default:
                    seenRegularName = true;
                    deque.add(name);
            }
        }

        final String[] newNames = deque.isEmpty() && root == null
            ? new String[]{ "" }
            : deque.stream().toArray(String[]::new);
        return builder.apply(root, newNames);
    }

    @Override
    protected String[] relativizeNames(final String[] names,
        final String[] otherNames)
    {
        final int namesLength = names.length;
        final int otherNamesLength = otherNames.length;
        final Collection<String> list = new ArrayList<>(namesLength
            + otherNamesLength);

        final int minSize = Math.min(namesLength, otherNamesLength);
        int nextIndex = 0;

        /*
         * While the elements at the same position are equal in both name sets,
         * we keep going, until at least one of them is exhausted
         */
        for (; nextIndex < minSize; nextIndex++)
            if (!names[nextIndex].equals(otherNames[nextIndex]))
                break;

        /*
         * We have swallowed all common elements.
         *
         * For each remaining element in the first name elements, we insert the
         * parent token.
         */
        for (int i = nextIndex; i < namesLength; i++)
            list.add(PARENT_TOKEN);

        /*
         * Now we insert all remaining tokens from the other name elements,
         * starting from the index which we computed
         */
        for (int i = nextIndex; i < otherNamesLength; i++)
            list.add(otherNames[i]);

        return list.stream().toArray(String[]::new);
    }
}
