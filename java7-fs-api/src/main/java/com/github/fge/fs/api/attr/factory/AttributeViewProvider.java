package com.github.fge.fs.api.attr.factory;

import com.github.fge.fs.api.attr.byname.NameDispatcher;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttributeView;

public interface AttributeViewProvider<V extends FileAttributeView>
{
    String getName();

    Class<V> getViewClass();

    V getView(Path path);

    NameDispatcher getNameDispatcher(Path path)
        throws IOException;
}
