package com.github.fge.fs.api.provider;

import com.github.fge.fs.api.attr.StandardAttributeViewNames;
import com.github.fge.fs.api.attr.byname.NameDispatcher;
import com.github.fge.fs.api.attr.factory.AttributeViewProvider;
import com.github.fge.fs.api.attr.factory.AttributesProvider;
import com.github.fge.fs.api.attr.factory.FileAttributeViewFactory;
import com.github.fge.fs.api.driver.FileSystemDriver;
import com.github.fge.fs.api.driver.FileSystemIoDriver;
import com.github.fge.fs.api.driver.FileSystemOptionsChecker;
import com.github.fge.fs.api.fs.AbstractFileSystem;
import com.github.fge.fs.api.internal.VisibleForTesting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Default abstract implementation of a {@link FileSystemProvider}
 *
 * <p>Please see the {@code LIMITATIONS.md} file on the <a
 * href="https://github.com/fge/java7-filesystems">project page</a> for the
 * enforced limitations of this abstract implementation.</p>
 */
@SuppressWarnings("CastToConcreteClass")
public abstract class AbstractFileSystemProvider
    extends FileSystemProvider
{
    private static final Pattern SLASH = Pattern.compile("/");
    private static final String ALL_ATTRS = "*";
    private static final Pattern COMMA = Pattern.compile(",");

    private final Map<URI, AbstractFileSystem> fileSystems = new HashMap<>();

    private final FileSystemFactory factory;

    protected AbstractFileSystemProvider(final FileSystemFactory factory)
    {
        this.factory = factory;
    }

    @Override
    public final String getScheme()
    {
        return factory.getScheme();
    }

    @Override
    public final FileSystem newFileSystem(final URI uri,
        final Map<String, ?> env)
        throws IOException
    {
        synchronized (fileSystems) {
            if (fileSystems.get(uri) != null)
                throw new FileSystemAlreadyExistsException(uri.toString());
            final AbstractFileSystem fs
                = factory.createFileSystem(this, uri, env);
            fileSystems.put(uri, fs);
            return fs;
        }
    }

    @Override
    public FileSystem getFileSystem(final URI uri)
    {
        return Optional.ofNullable(fileSystems.get(uri))
            .orElseThrow(FileSystemNotFoundException::new);
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
        final Set<OpenOption> optionSet = checker.getWriteOptions(options);
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
        return getDriver(dir).getIoDriver().getDirectoryStream(dir, filter);
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
        getDriver(path).getIoDriver().delete(path);
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
    protected static void copySameFs(final Path source, final Path target,
        final CopyOption... options)
        throws IOException
    {
        final FileSystemDriver driver = getDriver(source);
        final FileSystemOptionsChecker checker = driver.getOptionsChecker();
        final FileSystemIoDriver io = driver.getIoDriver();
        final Set<CopyOption> optionSet = checker.getCopyOptions(options);
        io.copy(source, target, optionSet);
    }

    @VisibleForTesting
    protected static void copyDifferentFs(final Path source, final Path target,
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
        final FileSystem sourceFs = source.getFileSystem();
        final FileSystem targetFs = target.getFileSystem();

        if (sourceFs.equals(targetFs))
            moveSameFs(source, target, options);
        else
            moveDifferentFs(source, target, options);
    }

    private static void moveSameFs(final Path source, final Path target,
        final CopyOption... options)
        throws IOException
    {
        final FileSystemDriver driver = getDriver(source);
        final FileSystemOptionsChecker checker = driver.getOptionsChecker();
        final FileSystemIoDriver io = driver.getIoDriver();
        final Set<CopyOption> optionSet = checker.getCopyOptions(options);
        io.move(source, target, optionSet);
    }

    private void moveDifferentFs(final Path source, final Path target,
        final CopyOption... options)
        throws IOException
    {
        copyDifferentFs(source, target, options);
        delete(source);
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
        // TODO! no idea what to do here
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
        getDriver(path).getIoDriver().checkAccess(path, modes);
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(final Path path,
        final Class<V> type, final LinkOption... options)
    {
        final FileAttributeViewFactory factory = getAttributeFactory(path);

        final AttributeViewProvider<V> provider
            = factory.getProviderForClass(type);

        if (provider == null)
            throw new UnsupportedOperationException();

        return provider.getView(path);
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(final Path path,
        final Class<A> type, final LinkOption... options)
        throws IOException
    {
        final FileAttributeViewFactory factory = getAttributeFactory(path);

        final AttributesProvider<A> provider
            = factory.getAttributesProvider(type);

        if (provider == null)
            throw new UnsupportedOperationException();

        return provider.getAttributes(path);
    }

    @Override
    public Map<String, Object> readAttributes(final Path path,
        final String attributes,
        final LinkOption... options)
        throws IOException
    {
        final String name;
        final String attrs;
        final int index = attributes.indexOf(':');

        if (index == -1) {
            name = StandardAttributeViewNames.BASIC;
            attrs = attributes;
        } else {
            name = attributes.substring(0, index);
            attrs = attributes.substring(index + 1);
        }

        final FileAttributeViewFactory factory = getAttributeFactory(path);

        final AttributeViewProvider<?> provider
            = factory.getProviderForName(name);

        if (provider == null)
            throw new UnsupportedOperationException();

        final NameDispatcher dispatcher = provider.getNameDispatcher(path);

        if (ALL_ATTRS.equals(attributes))
            return dispatcher.readAllAttributes();

        final Map<String, Object> map = new HashMap<>();

        for (final String attr: COMMA.split(attrs))
            map.put(attr, dispatcher.readByName(attr));

        return Collections.unmodifiableMap(map);
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
    protected static FileSystemDriver getDriver(final Path path)
    {
        final AbstractFileSystem fs = (AbstractFileSystem) path.getFileSystem();
        return fs.getDriver();
    }

    private static FileAttributeViewFactory getAttributeFactory(final Path path)
    {
        return ((AbstractFileSystem) path.getFileSystem())
            .getAttributeFactory();
    }
}
