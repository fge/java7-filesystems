package com.github.fge.fs.api.path;

import com.github.fge.fs.api.path.elements.PathElements;
import com.github.fge.fs.api.path.elements.PathElementsFactory;

import java.net.URI;

public interface PathContext
{
    URI getBaseUri();

    String getSeparator();

    PathElements getRootElements();

    PathElementsFactory getElementsFactory();
}
