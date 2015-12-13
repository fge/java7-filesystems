package com.github.fge.fs.api.path;

import com.github.fge.fs.api.path.elements.PathElements;
import com.github.fge.fs.api.path.elements.PathElementsFactory;

public interface PathContext
{
    PathElements getRootElements();

    PathElementsFactory getElementsFactory();
}
