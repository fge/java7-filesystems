package com.github.fge.fs.api.attr.byname;

import java.io.IOException;

@FunctionalInterface
public interface NamedAttributeReader
{
    Object read()
        throws IOException;
}
