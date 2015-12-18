package com.github.fge.fs.api.attr.byname;

import java.io.IOException;

@FunctionalInterface
public interface NamedAttributeWriter<T>
{
    void write(T value)
        throws IOException;
}
