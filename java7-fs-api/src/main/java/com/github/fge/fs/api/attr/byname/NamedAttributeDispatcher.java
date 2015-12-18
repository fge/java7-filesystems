package com.github.fge.fs.api.attr.byname;

import java.nio.file.attribute.FileAttributeView;

public abstract class NamedAttributeDispatcher<V extends FileAttributeView>
    implements NameDispatcher
{
    protected final V view;

    protected NamedAttributeDispatcher(final V view)
    {
        this.view = view;
    }
}
