package com.github.fge.fs.api.attr.load;

import java.nio.file.Path;
import java.nio.file.attribute.FileAttributeView;

public interface FileAttributeViewLoader<V extends FileAttributeView>
{
    V load(Path path)
        throws Exception;
}
