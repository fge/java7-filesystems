package com.github.fge.fs.api.filestore;

import com.github.fge.fs.api.attr.factory.FileAttributeViewFactory;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;

/**
 * Basic, abstract implementation of a {@link FileStore}
 *
 * <p>Please see the {@code LIMITATIONS.md} file on the <a
 * href="https://github.com/fge/java7-filesystems">project page</a> for the
 * enforced limitations of this abstract implementation.</p>
 *
 * <p>Additionally, it should be noted that the API documentation does not
 * specify what value should be returned for methods returning file store sizes
 * if an implementation is unable to compute it; this implementation chooses to
 * return {@link Long#MAX_VALUE} in this case. The three methods affected are:
 * </p>
 *
 * <ul>
 *     <li>{@link #getTotalSpace()};</li>
 *     <li>{@link #getUnallocatedSpace()};</li>
 *     <li>{@link #getUsableSpace()}.</li>
 * </ul>
 */
public abstract class AbstractFileStore
    extends FileStore
{
    protected final String name;
    protected final String type;
    protected final FileAttributeViewFactory fileAttributeViewFactory;

    protected AbstractFileStore(final String name, final String type,
        final FileAttributeViewFactory fileAttributeViewFactory)
    {
        this.name = name;
        this.type = type;
        this.fileAttributeViewFactory = fileAttributeViewFactory;
    }

    protected AbstractFileStore(final String name,
        final FileAttributeViewFactory fileAttributeViewFactory)
    {
        this(name, name, fileAttributeViewFactory);
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public String type()
    {
        return type;
    }

    @Override
    public boolean isReadOnly()
    {
        // TODO
        return false;
    }

    @Override
    public long getTotalSpace()
        throws IOException
    {
        return Long.MAX_VALUE;
    }

    @Override
    public long getUsableSpace()
        throws IOException
    {
        return Long.MAX_VALUE;
    }

    @Override
    public long getUnallocatedSpace()
        throws IOException
    {
        return Long.MAX_VALUE;
    }

    @Override
    public boolean supportsFileAttributeView(
        final Class<? extends FileAttributeView> type)
    {
        return fileAttributeViewFactory.getProviderForClass(type) != null;
    }

    @Override
    public boolean supportsFileAttributeView(final String name)
    {
        return fileAttributeViewFactory.getProviderForName(name) != null;
    }

    @Override
    public <V extends FileStoreAttributeView> V getFileStoreAttributeView(
        final Class<V> type)
    {
        return null;
    }

    @Override
    public Object getAttribute(final String attribute)
        throws IOException
    {
        throw new UnsupportedOperationException();
    }
}
