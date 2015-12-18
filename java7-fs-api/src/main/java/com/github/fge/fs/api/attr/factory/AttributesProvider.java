package com.github.fge.fs.api.attr.factory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public interface AttributesProvider<A extends BasicFileAttributes>
{
    Class<A> getAttributesClass();

    A getAttributes(Path path)
        throws IOException;
}
