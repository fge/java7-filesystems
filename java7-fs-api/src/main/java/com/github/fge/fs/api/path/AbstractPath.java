package com.github.fge.fs.api.path;

import com.github.fge.fs.api.FileSystemMismatchException;
import com.github.fge.fs.api.fs.AbstractFileSystem;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collections;
import java.util.Iterator;

/**
 * Basic abstract {@link Path} implementation
 *
 * <p>Please see the {@code LIMITATIONS.md} file on the <a
 * href="https://github.com/fge/java7-filesystems">project page</a> for the
 * enforced limitation of this abstract implementation.</p>
 *
 * <p>Please note that {@link #relativize(Path)} will throw an {@link
 * IllegalArgumentException} if either path has a {@link #getRoot() root
 * component}.</p>
 */
public abstract class AbstractPath
    implements PathBase
{
    protected final AbstractFileSystem fileSystem;

    protected AbstractPath(final AbstractFileSystem fileSystem)
    {
        this.fileSystem = fileSystem;
    }

    @Override
    public FileSystem getFileSystem()
    {
        return fileSystem;
    }

    protected abstract boolean isEmpty();

    @Override
    public boolean isAbsolute()
    {
        return getRoot() != null;
    }

    @Override
    public Path getRoot()
    {
        // TODO
        return null;
    }

    @Override
    public Path getFileName()
    {
        // TODO
        return null;
    }

    @Override
    public Path getParent()
    {
        // TODO
        return null;
    }

    @Override
    public int getNameCount()
    {
        // TODO
        return 0;
    }

    @Override
    public Path getName(final int index)
    {
        // TODO
        return null;
    }

    @Override
    public Path subpath(final int beginIndex, final int endIndex)
    {
        // TODO
        return null;
    }

    @Override
    public boolean startsWith(final Path other)
    {
        checkSameFileSystem(other);
        return doStartsWith((AbstractPath) other);
    }

    protected abstract boolean doStartsWith(AbstractPath other);

    @Override
    public boolean endsWith(final Path other)
    {
        checkSameFileSystem(other);
        return doEndsWith((AbstractPath) other);
    }

    protected abstract boolean doEndsWith(AbstractPath other);

    @Override
    public Path normalize()
    {
        // TODO
        return null;
    }

    @Override
    public Path resolve(final Path other)
    {
        checkSameFileSystem(other);
        if (other.isAbsolute())
            return other;
        final AbstractPath otherPath = (AbstractPath) other;
        if (otherPath.isEmpty())
            return this;
        return doResolve(otherPath);
    }

    /**
     * Perform name resolution of this path against another path on the same
     * filesystem
     *
     * <p>When this method is invoked, we know that the other path is neither
     * absolute nor the empty path.</p>
     *
     * @param other the other path
     * @return the resolved path
     */
    protected abstract Path doResolve(AbstractPath other);

    @Override
    public Path relativize(final Path other)
    {
        checkSameFileSystem(other);
        if (getRoot() != null || other.getRoot() != null)
            throw new IllegalArgumentException();
        return doRelativize((AbstractPath) other);
    }

    /**
     * Perform relativization of this path against another path
     *
     * <p>When this method is invoked, we know that no path (either this one or
     * the other path) have a {@link #getRoot() root component}.</p>
     *
     * @param other the other path
     * @return the relativized path
     */
    protected abstract Path doRelativize(AbstractPath other);

    @Override
    public URI toUri()
    {
        // TODO
        return null;
    }

    @Override
    public Path toAbsolutePath()
    {
        // TODO
        return null;
    }

    @Override
    public Path toRealPath(final LinkOption... options)
        throws IOException
    {
        return toAbsolutePath();
    }

    @Override
    public File toFile()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public WatchKey register(final WatchService watcher,
        final WatchEvent.Kind<?>[] events,
        final WatchEvent.Modifier... modifiers)
        throws IOException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Path> iterator()
    {
        return isEmpty() ? Collections.<Path>singleton(this).iterator()
            : new PathIterator(this);
    }

    @Override
    public int compareTo(final Path other)
    {
        checkSameFileSystem(other);
        return toString().compareTo(other.toString());
    }

    private void checkSameFileSystem(final Path other)
    {
        if (!getFileSystem().equals(other.getFileSystem()))
            throw new FileSystemMismatchException();
    }
}
