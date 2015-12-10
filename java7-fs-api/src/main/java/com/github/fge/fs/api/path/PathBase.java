package com.github.fge.fs.api.path;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 * An extesion of the {@link Path} interface with default implementations
 *
 * <p>The default implementations are those implemented in Java 9.</p>
 *
 */
public interface PathBase
    extends Path
{
    @Override
    default boolean endsWith(final String other)
    {
        return endsWith(getFileSystem().getPath(other));
    }

    @Override
    default Path resolve(final String other)
    {
        return resolve(getFileSystem().getPath(other));
    }

    @Override
    default Path resolveSibling(final Path other)
    {
        if (other == null)
            throw new NullPointerException();
        final Path parent = getParent();
        return parent == null ? other : parent.resolve(other);
    }

    @Override
    default Path resolveSibling(final String other)
    {
        return resolveSibling(getFileSystem().getPath(other));
    }

    @SuppressWarnings("ProblematicVarargsMethodOverride")
    @Override
    default WatchKey register(final WatchService watcher,
        final WatchEvent.Kind<?>[] events)
        throws IOException
    {
        return register(watcher, events,  new WatchEvent.Modifier[0]);
    }

    @Override
    default boolean startsWith(final String other)
    {
        return startsWith(getFileSystem().getPath(other));
    }
}
