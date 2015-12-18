package com.github.fge.fs.api.filestore;

import com.github.fge.fs.api.attr.factory.FileAttributeViewFactory;

/**
 * Basic implementation of a read only {@link AbstractFileStore}
 *
 * <p>The only difference with the base implementation is that {@link
 * #isReadOnly()} always returns true.</p>
 */
public abstract class AbstractReadOnlyFileStore
    extends AbstractFileStore
{
    protected AbstractReadOnlyFileStore(final String name, final String type,
        final FileAttributeViewFactory fileAttributeViewFactory)
    {
        super(name, type, fileAttributeViewFactory);
    }

    protected AbstractReadOnlyFileStore(final String name,
        final FileAttributeViewFactory fileAttributeViewFactory)
    {
        super(name, fileAttributeViewFactory);
    }

    @Override
    public final boolean isReadOnly()
    {
        return true;
    }
}
