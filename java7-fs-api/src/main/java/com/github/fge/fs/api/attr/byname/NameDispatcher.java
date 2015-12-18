package com.github.fge.fs.api.attr.byname;

import java.io.IOException;
import java.util.Map;

public interface NameDispatcher
{
    Object readByName(String name)
        throws IOException;

    void writeByBame(String name, Object value)
        throws IOException;

    Map<String, Object> readAllAttributes()
        throws IOException;
}
