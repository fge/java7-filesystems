package com.github.fge.fs.api.attr.factory;

import com.github.fge.fs.api.attr.load.BasicLazyFileAttributeView;
import com.github.fge.fs.api.attr.load.FileAttributeViewLoader;

import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;

public class BasicFileAttributesProvider
    extends AbstractAttributesProvider<BasicFileAttributeView, BasicFileAttributes>
{
    protected BasicFileAttributesProvider(
        final FileAttributeViewLoader<BasicFileAttributeView> loader)
    {
        super(BasicFileAttributes.class, loader,
            BasicLazyFileAttributeView::new);
    }
}
