package com.github.fge.fs.api.path;

import com.github.fge.fs.api.FileSystemMismatchException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;

public abstract class AbstractPath
    implements PathBase
{
    @Override
    public FileSystem getFileSystem()
    {
        // TODO
        return null;
    }

    /**
     * @implNote returns true if and only if the path has no {@link #getRoot()
     * root}
     *
     * @return see description
     */
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

    /**
     * @implNote enforces the same filesystem policy; otherwise, behaves the
     * same as {@link Path#startsWith(String)}.
     *
     * @param other the other path
     * @return see description
     */
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

    /**
     * @implNote enforces the same filesystem policy; otherwise, behaves the
     * same as {@link Path#resolve(Path)}
     *
     * @param other the other path
     * @return see description
     */
    @Override
    public Path resolve(final Path other)
    {
        checkSameFileSystem(other);
        return doResolve((AbstractPath) other);
    }

    protected abstract Path doResolve(AbstractPath other);

    /**
     * @implNote enforces the same filesystem policy; otherwise, behaves the
     * same as {@link Path#relativize(Path)}
     *
     * @param other the other path
     * @return see description
     */
    @Override
    public Path relativize(final Path other)
    {
        checkSameFileSystem(other);
        return doRelativize((AbstractPath) other);
    }

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

    /**
     * @implNote returns the result of {@link #toAbsolutePath()}
     *
     * @param options ignored
     * @return see description
     * @throws IOException never thrown here
     */
    @Override
    public Path toRealPath(final LinkOption... options)
        throws IOException
    {
        return toAbsolutePath();
    }

    /**
     * @implNote always throws {@link UnsupportedOperationException}
     *
     * @return see description
     */
    @Override
    public File toFile()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * @implNote always throws {@link UnsupportedOperationException}
     *
     * @param watcher ignored
     * @param events ignored
     * @param modifiers ignored
     * @return see description
     * @throws IOException not thrown here; see description
     */
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
        // TODO
        return null;
    }

    /**
     * @implNote enforces the "same file system" policy; if this passes, does a
     * simple string lexicographical comparison of the two paths's {@link
     * #toString() string representation}.
     *
     * @param other the other path
     * @return see description
     * @throws FileSystemMismatchException paths are from different filesystems
     */
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
