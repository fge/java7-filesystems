package com.github.fge.fs.api.attr.byname;


import com.github.fge.fs.api.attr.StandardAttributeNames;

import java.nio.file.attribute.FileOwnerAttributeView;

public class FileOwnerAttributeDispatcher<V extends FileOwnerAttributeView>
    extends DiscreteNamedAttributeDispatcher<V>
{
    public FileOwnerAttributeDispatcher(final V view)
    {
        super(view);
        registerReader(StandardAttributeNames.OWNER, view::getOwner);
        registerWriter(StandardAttributeNames.OWNER, view::setOwner);
    }
}
