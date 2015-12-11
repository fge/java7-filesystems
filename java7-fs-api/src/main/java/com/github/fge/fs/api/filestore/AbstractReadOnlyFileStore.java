package com.github.fge.fs.api.filestore;

/**
 * Basic implementation of a read only {@link AbstractFileStore}
 *
 * <p>The only difference with the base implementation is that {@link
 * #isReadOnly()} always returns true.</p>
 */
public abstract class AbstractReadOnlyFileStore
    extends AbstractFileStore
{
    protected AbstractReadOnlyFileStore(final String name, final String type)
    {
        super(name, type);
    }

    protected AbstractReadOnlyFileStore(final String name)
    {
        super(name);
    }

    @Override
    public final boolean isReadOnly()
    {
        return true;
    }
}
