package com.github.fge.fs.api.path.elements;

public interface PathElementsFactory
{
    PathElements buildElements(String input);

    // TODO: move elsewhere?
    PathElements getFileSystemRoot();

    PathElements getRoot(PathElements elements);

    PathElements getFileName(PathElements elements);

    PathElements getParent(PathElements elements);

    PathElements getName(PathElements elements, int index);

    PathElements getNames(PathElements elements, int beginIndex, int endIndex);

    boolean startsWith(PathElements elements, PathElements otherElements);

    boolean endsWith(PathElements elements, PathElements otherElements);

    PathElements normalize(PathElements elements);

    PathElements resolve(PathElements elements, PathElements otherElements);

    PathElements relativize(PathElements elements, PathElements otherElements);
}
