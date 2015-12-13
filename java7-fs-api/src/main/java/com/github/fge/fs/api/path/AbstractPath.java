package com.github.fge.fs.api.path;

import com.github.fge.fs.api.FileSystemMismatchException;
import com.github.fge.fs.api.fs.AbstractFileSystem;
import com.github.fge.fs.api.path.elements.PathElements;
import com.github.fge.fs.api.path.elements.PathElementsFactory;

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
import java.util.Objects;

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
    protected final PathElements pathElements;
    protected final PathElementsFactory factory;

    protected AbstractPath(final AbstractFileSystem fileSystem,
        final PathElements pathElements)
    {
        this.fileSystem = fileSystem;
        this.pathElements = pathElements;
        factory = fileSystem.getPathElementsFactory();
    }

    @Override
    public FileSystem getFileSystem()
    {
        return fileSystem;
    }

    protected boolean isEmpty()
    {
        return pathElements.isEmptyElements();
    }

    @Override
    public boolean isAbsolute()
    {
        return getRoot() != null;
    }

    @Override
    public Path getRoot()
    {
        final PathElements elements = factory.getRoot(pathElements);
        return fileSystem.buildPath(elements);
    }

    @Override
    public Path getFileName()
    {
        final PathElements elements = factory.getFileName(pathElements);
        return fileSystem.buildPath(elements);
    }

    @Override
    public Path getParent()
    {
        final PathElements elements = factory.getParent(pathElements);
        return fileSystem.buildPath(elements);
    }

    @Override
    public int getNameCount()
    {
        return pathElements.getNames().length;
    }

    @Override
    public Path getName(final int index)
    {
        final PathElements elements = factory.getName(pathElements, index);
        return fileSystem.buildPath(elements);
    }

    @Override
    public Path subpath(final int beginIndex, final int endIndex)
    {
        final PathElements elements = factory.getNames(pathElements, beginIndex,
            endIndex);
        return fileSystem.buildPath(elements);
    }

    @Override
    public boolean startsWith(final Path other)
    {
        checkSameFileSystem(other);
        final AbstractPath other1 = (AbstractPath) other;
        final PathElements otherElements = other1.pathElements;
        return factory.startsWith(pathElements, otherElements);
    }

    @Override
    public boolean endsWith(final Path other)
    {
        checkSameFileSystem(other);
        final AbstractPath other1 = (AbstractPath) other;
        final PathElements otherElements = other1.pathElements;
        return factory.endsWith(pathElements, otherElements);
    }

    @Override
    public Path normalize()
    {
        final PathElements elements = factory.normalize(pathElements);
        return fileSystem.buildPath(elements);
    }

    @Override
    public Path resolve(final Path other)
    {
        checkSameFileSystem(other);
        final AbstractPath otherPath = (AbstractPath) other;
        final PathElements otherElements = otherPath.pathElements;
        final PathElements newElements = factory.resolve(pathElements,
            otherElements);
        return fileSystem.buildPath(newElements);
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
        final AbstractPath otherPath = (AbstractPath) other;
        final PathElements otherElements = otherPath.pathElements;
        final PathElements newElements = factory.relativize(pathElements,
            otherElements);
        return fileSystem.buildPath(newElements);
    }

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

    @Override
    public int hashCode()
    {
        return Objects.hash(fileSystem, pathElements);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (!(obj instanceof AbstractPath))
            return false;
        final AbstractPath other = (AbstractPath) obj;
        return fileSystem.equals(other.fileSystem)
            && pathElements.equals(other.pathElements);
    }

    @Override
    public final String toString()
    {
        return pathElements.toString();
    }

    private void checkSameFileSystem(final Path other)
    {
        if (!getFileSystem().equals(other.getFileSystem()))
            throw new FileSystemMismatchException();
    }
}
