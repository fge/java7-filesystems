package com.github.fge.fs.api.attr.load;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;

public interface FileAttributesProvider<A extends BasicFileAttributes>
{
    A readAttributes()
        throws IOException;
}
