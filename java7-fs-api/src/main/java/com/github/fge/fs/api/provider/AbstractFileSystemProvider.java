package com.github.fge.fs.api.provider;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
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
    @Override
    public String getScheme()
    {
        // TODO
        return null;
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
    public SeekableByteChannel newByteChannel(final Path path,
        final Set<? extends OpenOption> options,
        final FileAttribute<?>... attrs)
        throws IOException
    {
        // TODO
        return null;
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
        // TODO

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
        // TODO

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
        // TODO
        return false;
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
}
