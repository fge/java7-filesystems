package com.github.fge.fs.api.fs;

import com.github.fge.fs.api.attr.factory.FileAttributeViewFactory;
import com.github.fge.fs.api.driver.FileSystemDriver;
import com.github.fge.fs.api.filestore.AbstractFileStore;
import com.github.fge.fs.api.path.PathContext;
import com.github.fge.fs.api.path.elements.PathElements;
import com.github.fge.fs.api.path.elements.PathElementsFactory;
import com.github.fge.fs.api.provider.AbstractFileSystemProvider;

import java.io.IOException;
import java.nio.file.ClosedFileSystemException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Default abstract implementation of a {@link FileSystem}
 *
 * <p>Please see the {@code LIMITATIONS.md} file on the <a
 * href="https://github.com/fge/java7-filesystems">project page</a> for the
 * enforced limitations of this abstract implementation.</p>
 */
public abstract class AbstractFileSystem
    extends FileSystem
{
    protected final AtomicBoolean open = new AtomicBoolean(true);

    protected final AbstractFileSystemProvider provider;
    protected final AbstractFileStore fileStore;
    protected final PathContext pathContext;
    protected final FileSystemDriver driver;

    protected AbstractFileSystem(final AbstractFileSystemProvider provider,
        final AbstractFileStore fileStore, final PathContext pathContext,
        final FileSystemDriver driver)
    {
        this.provider = Objects.requireNonNull(provider);
        this.fileStore = Objects.requireNonNull(fileStore);
        this.pathContext = Objects.requireNonNull(pathContext);
        this.driver = Objects.requireNonNull(driver);
    }

    public abstract Path buildPath(final PathElements elements);

    @Override
    public FileSystemProvider provider()
    {
        return provider;
    }

    public final FileSystemDriver getDriver()
    {
        ensureOpen();
        return driver;
    }

    public final FileAttributeViewFactory getViewFactory()
    {
        ensureOpen();
        return fileStore.getViewFactory();
    }

    @Override
    public final void close()
        throws IOException
    {
        if (open.getAndSet(false))
            return;

        try {
            driver.close();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public boolean isOpen()
    {
        return open.get();
    }

    @Override
    public boolean isReadOnly()
    {
        ensureOpen();
        return fileStore.isReadOnly();
    }

    @Override
    public String getSeparator()
    {
        ensureOpen();
        return pathContext.getSeparator();
    }

    @Override
    public Iterable<Path> getRootDirectories()
    {
        ensureOpen();
        final Path path = buildPath(pathContext.getRootElements());
        return Collections.singleton(path);
    }

    @Override
    public Iterable<FileStore> getFileStores()
    {
        ensureOpen();
        return Collections.singleton(fileStore);
    }

    @Override
    public Set<String> supportedFileAttributeViews()
    {
        ensureOpen();
        // TODO: use information from the AbstractFileStore
        return null;
    }

    @Override
    public Path getPath(final String first, final String... more)
    {
        ensureOpen();
        final PathElementsFactory factory = pathContext.getElementsFactory();
        PathElements elements = factory.buildElements(first);

        PathElements otherElements;

        for (final String s: more) {
            otherElements = factory.buildElements(s);
            elements = factory.resolve(elements, otherElements);
        }

        return buildPath(elements);
    }

    @Override
    public PathMatcher getPathMatcher(final String syntaxAndPattern)
    {
        // FIXME: contract violation
        throw new UnsupportedOperationException();
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public WatchService newWatchService()
        throws IOException
    {
        throw new UnsupportedOperationException();
    }

    private void ensureOpen()
    {
        if (!open.get())
            throw new ClosedFileSystemException();
    }
}
