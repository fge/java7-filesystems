package com.github.fge.fs.api.attr.factory;

import com.github.fge.fs.api.attr.StandardAttributeViewNames;
import com.github.fge.fs.api.attr.byname.BasicFileAttributeDispatcher;
import com.github.fge.fs.api.attr.load.BasicLazyFileAttributeView;
import com.github.fge.fs.api.attr.load.FileAttributeViewLoader;

import java.nio.file.attribute.BasicFileAttributeView;

public final class BasicAttributeViewProvider
    extends AbstractAttributeViewProvider<BasicFileAttributeView>
{
    public BasicAttributeViewProvider(
        final FileAttributeViewLoader<BasicFileAttributeView> loader)
    {
        super(StandardAttributeViewNames.BASIC, BasicFileAttributeView.class,
            loader, BasicLazyFileAttributeView::new,
            BasicFileAttributeDispatcher::new);
    }
}
