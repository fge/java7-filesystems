package com.github.fge.fs.api.attr.load;

import com.github.fge.fs.api.attr.views.BasicFileAttributeViewBase;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public final class BasicLazyFileAttributeView
    extends LazyFileAttributeView<BasicFileAttributeView>
    implements BasicFileAttributeViewBase, FileAttributesProvider<BasicFileAttributes>

{
    public BasicLazyFileAttributeView(
        final FileAttributeViewLoader<BasicFileAttributeView> loader,
        final Path path)
    {
        super(loader, path);
    }

    @Override
    public BasicFileAttributes readAttributes()
        throws IOException
    {
        return loadView().readAttributes();
    }

    @Override
    public void setTimes(final FileTime lastModifiedTime,
        final FileTime lastAccessTime, final FileTime createTime)
        throws IOException
    {
        loadView().setTimes(lastModifiedTime, lastAccessTime, createTime);
    }
}
