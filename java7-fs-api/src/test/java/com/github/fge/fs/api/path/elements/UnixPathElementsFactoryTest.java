package com.github.fge.fs.api.path.elements;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public final class UnixPathElementsFactoryTest
{
    private PathElementsFactory factory;

    @BeforeMethod
    public void initFactory()
    {
        factory = new UnixPathElementsFactory();
    }

    @DataProvider
    public Iterator<Object[]> buildElementsData()
    {
        final List<Object[]> list = new ArrayList<>();

        String input;
        String root;
        String[] names;

        input = "";
        root = null;
        names = new String[] { "" };
        list.add(new Object[] { input, root, names });

        input = "/";
        root = "/";
        names = new String[0];
        list.add(new Object[] { input, root, names });

        input = "a/../b";
        root = null;
        names = new String[] { "a", "..", "b" };
        list.add(new Object[] { input, root, names });

        input = "./";
        root = null;
        names = new String[] { "." };
        list.add(new Object[] { input, root, names });

        input = "//proc/242//fd";
        root = "/";
        names = new String[] { "proc", "242", "fd" };
        list.add(new Object[] { input, root, names });

        Collections.shuffle(list);
        return list.iterator();
    }

    @Test(dataProvider = "buildElementsData")
    public void buildElementsTest(final String input, final String root,
        final String[] names)
    {
        final PathElements elements = factory.buildElements(input);

        try (
            final AutoCloseableSoftAssertions soft
                = new AutoCloseableSoftAssertions();
        ) {
            soft.assertThat(elements.getRoot()).isEqualTo(root);
            soft.assertThat(elements.getNames()).containsExactly(names);
        }
    }

    @DataProvider
    public Iterator<Object[]> getRootData()
    {
        final List<Object[]> list = new ArrayList<>();

        String input;
        String expected;

        input = "a/b";
        expected = null;
        list.add(new Object[] { input, expected });

        input = "/foo/bar";
        expected = "/";
        list.add(new Object[] { input, expected });

        Collections.shuffle(list);
        return list.iterator();
    }

    @Test(dataProvider = "getRootData")
    public void getRootTest(final String input, final String expected)
    {
        final PathElements elements = factory.buildElements(input);

        if (expected == null)
            assertThat(factory.getRoot(elements)).isNull();
        else
            assertThat(factory.getRoot(elements))
                .isEqualTo(factory.buildElements(expected));
    }

    @DataProvider
    public Iterator<Object[]> getFileNameData()
    {
        final List<Object[]> list = new ArrayList<>();

        String input;
        String expected;

        input = "a/b";
        expected = "b";
        list.add(new Object[] { input, expected });

        input = "/foo/bar";
        expected = "bar";
        list.add(new Object[] { input, expected });

        input = "";
        expected = "";
        list.add(new Object[] { input, expected });

        input = "/";
        expected = null;
        list.add(new Object[] { input, expected });

        Collections.shuffle(list);
        return list.iterator();
    }

    @Test(dataProvider = "getFileNameData")
    public void getFileNameTest(final String input, final String expected)
    {
        final PathElements elements = factory.buildElements(input);

        if (expected == null)
            assertThat(factory.getFileName(elements)).isNull();
        else
            assertThat(factory.getFileName(elements))
                .isEqualTo(factory.buildElements(expected));
    }

    @DataProvider
    public Iterator<Object[]> getParentData()
    {
        final List<Object[]> list = new ArrayList<>();

        String input;
        String expected;

        input = "";
        expected = null;
        list.add(new Object[] { input, expected });

        input = "/";
        expected = null;
        list.add(new Object[] { input, expected });

        input = "a";
        expected = null;
        list.add(new Object[] { input, expected });

        input = "/a";
        expected = "/";
        list.add(new Object[] { input, expected });

        Collections.shuffle(list);
        return list.iterator();
    }

    @Test(dataProvider = "getParentData")
    public void getParentTest(final String input, final String expected)
    {
        final PathElements elements = factory.buildElements(input);

        if (expected == null)
            assertThat(factory.getParent(elements)).isNull();
        else
            assertThat(factory.getParent(elements))
                .isEqualTo(factory.buildElements(expected));
    }

    @DataProvider
    public Iterator<Object[]> getNameData()
    {
        final List<Object[]> list = new ArrayList<>();

        String input;
        int index;
        String expected;

        input = "";
        index = 0;
        expected = "";
        list.add(new Object[] { input, index, expected });

        input = "/a";
        index = 0;
        expected = "a";
        list.add(new Object[] { input, index, expected });

        input = "/a/b";
        index = 0;
        expected = "a";
        list.add(new Object[] { input, index, expected });

        input = "foo/../bar/n";
        index = 1;
        expected = "..";
        list.add(new Object[] { input, index, expected });

        Collections.shuffle(list);
        return list.iterator();
    }

    @Test(dataProvider = "getNameData")
    public void getNameTest(final String input, final int index,
        final String expected)
    {
        final PathElements elements = factory.buildElements(input);
        final PathElements name = factory.buildElements(expected);

        assertThat(factory.getName(elements, index)).isEqualTo(name);
    }

    @DataProvider
    public Iterator<Object[]> getNamesData()
    {
        final List<Object[]> list = new ArrayList<>();

        String input;
        int beginIndex;
        int endIndex;
        String expected;

        input = "";
        beginIndex = 0;
        endIndex = 1;
        expected = "";
        list.add(new Object[] { input, beginIndex, endIndex, expected });

        input = "/a/b/../c/d/";
        beginIndex = 2;
        endIndex = 5;
        expected = "../c/d";
        list.add(new Object[] { input, beginIndex, endIndex, expected });

        Collections.shuffle(list);
        return list.iterator();
    }

    @Test(dataProvider = "getNamesData")
    public void getNamesTest(final String input, final int beginIndex,
        final int endIndex, final String expected)
    {
        final PathElements elements = factory.buildElements(input);
        final PathElements names = factory.buildElements(expected);

        assertThat(factory.getNames(elements, beginIndex, endIndex))
            .isEqualTo(names);
    }

    @DataProvider
    public Iterator<Object[]> startsWithData()
    {
        final List<Object[]> list = new ArrayList<>();

        String first;
        String other;
        boolean result;

        first = "ab";
        other = "a";
        result = false;
        list.add(new Object[] { first, other, result });

        first = "a/b/c";
        other = "a/..";
        result = false;
        list.add(new Object[] { first, other, result });

        first = "foo/bar";
        other = "/foo/bar";
        result = false;
        list.add(new Object[] { first, other, result });

        first = "/foo/bar";
        other = "/foo/bar";
        result = true;
        list.add(new Object[] { first, other, result });

        first = "/foo/bar/baz";
        other = "/foo/bar";
        result = true;
        list.add(new Object[] { first, other, result });

        Collections.shuffle(list);
        return list.iterator();
    }

    @Test(dataProvider = "startsWithData")
    public void startsWithTest(final String first, final String other,
        final boolean result)
    {
        final PathElements elements = factory.buildElements(first);
        final PathElements otherElements = factory.buildElements(other);

        assertThat(factory.startsWith(elements, otherElements))
            .isEqualTo(result);
    }

    @DataProvider
    public Iterator<Object[]> endsWithData()
    {
        final List<Object[]> list = new ArrayList<>();

        String first;
        String other;
        boolean result;

        first = "ab";
        other = "a";
        result = false;
        list.add(new Object[] { first, other, result });

        first = "/";
        other = "/";
        result = true;
        list.add(new Object[] { first, other, result });

        first = "/a/b/c";
        other = "/a/b/c";
        result = true;
        list.add(new Object[] { first, other, result });

        first = "a/b/c";
        other = "/a/b/c";
        result = false;
        list.add(new Object[] { first, other, result });

        first = "/a/b/c";
        other = "a/b/c";
        result = true;
        list.add(new Object[] { first, other, result });

        first = "a/b/c/d";
        other = "c/d";
        result = true;
        list.add(new Object[] { first, other, result });

        first = "../c";
        other = "c";
        result = true;
        list.add(new Object[] { first, other, result });

        Collections.shuffle(list);
        return list.iterator();
    }

    @Test(dataProvider = "endsWithData")
    public void endsWithTest(final String first, final String other,
        final boolean result)
    {
        final PathElements elements = factory.buildElements(first);
        final PathElements otherElements = factory.buildElements(other);

        assertThat(factory.endsWith(elements, otherElements))
            .isEqualTo(result);
    }

    @DataProvider
    public Iterator<Object[]> normalizeData()
    {
        final List<Object[]> list = new ArrayList<>();

        String input;
        String normalized;

        input = "a/../b";
        normalized = "b";
        list.add(new Object[] { input, normalized });

        input = "/../../foo";
        normalized = "/foo";
        list.add(new Object[] { input, normalized });

        input = "/a/..";
        normalized = "/";
        list.add(new Object[] { input, normalized });

        input = "a/..";
        normalized = "";
        list.add(new Object[] { input, normalized });

        input = ".";
        normalized = "";
        list.add(new Object[] { input, normalized });

        input = "/.";
        normalized = "/";
        list.add(new Object[] { input, normalized });

        input = "./a/../foo/./bar/.";
        normalized = "foo/bar";
        list.add(new Object[] { input, normalized });

        Collections.shuffle(list);
        return list.iterator();
    }

    @Test(dataProvider = "normalizeData")
    public void normalizeTest(final String input, final String normalized)
    {
        final PathElements elements = factory.buildElements(input);
        final PathElements result = factory.buildElements(normalized);

        assertThat(factory.normalize(elements)).isEqualTo(result);
    }

    @DataProvider
    public Iterator<Object[]> resolveData()
    {
        final List<Object[]> list = new ArrayList<>();

        String first;
        String other;
        String resolved;

        first = "/foo";
        other = "/bar";
        resolved = "/bar";
        list.add(new Object[] { first, other, resolved });

        first = "/foo";
        other = "";
        resolved = "/foo";
        list.add(new Object[] { first, other, resolved });

        first = "";
        other = "a";
        resolved = "a";
        list.add(new Object[] { first, other, resolved });

        first = "1";
        other = "a/..";
        resolved = "1/a/..";
        list.add(new Object[] { first, other, resolved });

        Collections.shuffle(list);
        return list.iterator();
    }

    @Test(dataProvider = "resolveData")
    public void resolveTest(final String first, final String other,
        final String resolved)
    {
        final PathElements elements = factory.buildElements(first);
        final PathElements otherElements = factory.buildElements(other);
        final PathElements result = factory.buildElements(resolved);

        assertThat(factory.resolve(elements, otherElements)).isEqualTo(result);
    }

    @DataProvider
    public Iterator<Object[]> relativizeData()
    {
        final List<Object[]> list = new ArrayList<>();

        String first;
        String other;
        String relativized;

        first = "a";
        other = "b";
        relativized = "../b";
        list.add(new Object[] { first, other, relativized });

        first = "/a";
        other = "/b";
        relativized = "../b";
        list.add(new Object[] { first, other, relativized });

        first = "/a/b";
        other = "/a/b/c/d";
        relativized = "c/d";
        list.add(new Object[] { first, other, relativized });

        first = "a/b";
        other = "b";
        relativized = "../../b";
        list.add(new Object[] { first, other, relativized });

        first = "a/x";
        other = "a/c";
        relativized = "../c";
        list.add(new Object[] { first, other, relativized });

        first = "/a/b/c";
        other = "/a/b/c";
        relativized = "";
        list.add(new Object[] { first, other, relativized });

        Collections.shuffle(list);
        return list.iterator();
    }

    @Test(dataProvider = "relativizeData")
    public void relativizeTest(final String first, final String other,
        final String relativized)
    {
        final PathElements elements = factory.buildElements(first);
        final PathElements otherElements = factory.buildElements(other);
        final PathElements result = factory.buildElements(relativized);

        assertThat(factory.relativize(elements, otherElements))
            .isEqualTo(result);
    }
}
