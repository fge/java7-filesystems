package com.github.fge.fs.api.provider;

import com.github.fge.fs.api.driver.FileSystemDriver;
import com.github.fge.fs.api.driver.FileSystemIoDriver;
import com.github.fge.fs.api.driver.FileSystemOptionsChecker;
import com.github.fge.fs.api.internal.VisibleForTesting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.ClosedFileSystemException;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Default abstract implementation of a {@link FileSystemProvider}
 *
 * <p>Please see the {@code LIMITATIONS.md} file on the <a
 * href="https://github.com/fge/java7-filesystems">project page</a> for the
 * enforced limitations of this abstract implementation.</p>
 */
public abstract class AbstractFileSystemProvider
    extends FileSystemProvider
{
    protected final String scheme;

    @VisibleForTesting
    protected final ConcurrentMap<FileSystem, FileSystemDriver> fileSystems
        = new ConcurrentHashMap<>();

    protected AbstractFileSystemProvider(final String scheme)
    {
        this.scheme = scheme;
    }

    @Override
    public String getScheme()
    {
        return scheme;
    }

    @Override
    public FileSystem newFileSystem(final URI uri, final Map<String, ?> env)
        throws IOException
    {
        // TODO
        return null;
    }

    @Override
    public FileSystem getFileSystem(final URI uri)
    {
        // TODO
        return null;
    }

    @Override
    public Path getPath(final URI uri)
    {
        // TODO
        return null;
    }

    @Override
    public InputStream newInputStream(final Path path,
        final OpenOption... options)
        throws IOException
    {
        final FileSystemDriver driver = getDriver(path);
        final FileSystemOptionsChecker checker = driver.getOptionsChecker();
        final Set<OpenOption> optionSet = checker.getReadOptions(options);
        return driver.getIoDriver().getInputStream(path, optionSet);
    }

    @Override
    public OutputStream newOutputStream(final Path path,
        final OpenOption... options)
        throws IOException
    {
        final FileSystemDriver driver = getDriver(path);
        final FileSystemOptionsChecker checker = driver.getOptionsChecker();
        final Set<OpenOption> optionSet = checker.checkWriteOptions(options);
        return driver.getIoDriver().getOutputStream(path, optionSet);
    }

    @Override
    public SeekableByteChannel newByteChannel(final Path path,
        final Set<? extends OpenOption> options,
        final FileAttribute<?>... attrs)
        throws IOException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(final Path dir,
        final DirectoryStream.Filter<? super Path> filter)
        throws IOException
    {
        // TODO
        return null;
    }

    @Override
    public void createDirectory(final Path dir, final FileAttribute<?>... attrs)
        throws IOException
    {
        // TODO: hardcoded...
        if (attrs.length > 0)
            throw new UnsupportedOperationException();
        getDriver(dir).getIoDriver().createDirectory(dir);
    }

    @Override
    public void delete(final Path path)
        throws IOException
    {
        // TODO

    }

    @Override
    public void copy(final Path source, final Path target,
        final CopyOption... options)
        throws IOException
    {
        final FileSystem sourceFs = source.getFileSystem();
        final FileSystem targetFs = target.getFileSystem();

        if (sourceFs.equals(targetFs))
            copySameFs(source, target, options);
        else
            copyDifferentFs(source, target, options);
    }

    @VisibleForTesting
    protected void copySameFs(final Path source, final Path target,
        final CopyOption... options)
        throws IOException
    {
        final FileSystemDriver driver = getDriver(source);
        final FileSystemOptionsChecker checker = driver.getOptionsChecker();
        final FileSystemIoDriver io = driver.getIoDriver();
        final Set<CopyOption> optionSet = checker.checkCopyOptions(options);
        io.copy(source, target, optionSet);
    }

    @VisibleForTesting
    protected void copyDifferentFs(final Path source, final Path target,
        final CopyOption... options)
        throws IOException
    {
        final FileSystemDriver srcDriver = getDriver(source);
        final FileSystemOptionsChecker srcChecker
            = srcDriver.getOptionsChecker();
        final FileSystemIoDriver srcIo = srcDriver.getIoDriver();
        final FileSystemDriver dstDriver = getDriver(target);
        final FileSystemOptionsChecker dstChecker
            = dstDriver.getOptionsChecker();
        final FileSystemIoDriver dstIo = dstDriver.getIoDriver();

        final Set<OpenOption> srcOpts = srcChecker.copyToReadOptions(options);
        final Set<OpenOption> dstOpts = dstChecker.copyToWriteOptions(options);

        // FIXME: hardcoded
        final byte[] buf = new byte[8192];

        try (
            final InputStream in = srcIo.getInputStream(source, srcOpts);
            final OutputStream out = dstIo.getOutputStream(target, dstOpts);
        ) {
            int nrBytes;
            while ((nrBytes = in.read(buf)) != -1)
                out.write(buf, 0, nrBytes);
        }
    }

    @Override
    public void move(final Path source, final Path target,
        final CopyOption... options)
        throws IOException
    {
        // TODO

    }

    @Override
    public boolean isSameFile(final Path path, final Path path2)
        throws IOException
    {
        return path.toAbsolutePath().equals(path2.toAbsolutePath());
    }

    @Override
    public boolean isHidden(final Path path)
        throws IOException
    {
        // TODO
        return false;
    }

    @Override
    public FileStore getFileStore(final Path path)
        throws IOException
    {
        return path.getFileSystem().getFileStores().iterator().next();
    }

    @Override
    public void checkAccess(final Path path, final AccessMode... modes)
        throws IOException
    {
        // TODO

    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(final Path path,
        final Class<V> type, final LinkOption... options)
    {
        // TODO
        return null;
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(final Path path,
        final Class<A> type, final LinkOption... options)
        throws IOException
    {
        // TODO
        return null;
    }

    @Override
    public Map<String, Object> readAttributes(final Path path,
        final String attributes,
        final LinkOption... options)
        throws IOException
    {
        // TODO
        return null;
    }

    @Override
    public void setAttribute(final Path path, final String attribute,
        final Object value,
        final LinkOption... options)
        throws IOException
    {
        // TODO

    }

    @VisibleForTesting
    protected FileSystemDriver getDriver(final Path path)
    {
        final FileSystem fs = path.getFileSystem();
        if (!fs.isOpen())
            throw new ClosedFileSystemException();
        return getDriver(fs);
    }

    @VisibleForTesting
    protected FileSystemDriver getDriver(final FileSystem fs)
    {
        final FileSystemDriver driver = fileSystems.get(fs);
        if (driver == null)
            throw new ClosedFileSystemException();
        return driver;
    }
}
