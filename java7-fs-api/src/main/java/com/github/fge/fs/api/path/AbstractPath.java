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
        return doResolve((AbstractPath) other);
    }

    protected abstract Path doResolve(AbstractPath other);

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
        // TODO
        return null;
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
