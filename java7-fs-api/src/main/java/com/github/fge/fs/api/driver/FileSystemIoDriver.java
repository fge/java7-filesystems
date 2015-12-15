package com.github.fge.fs.api.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.ReadOnlyFileSystemException;
import java.util.Set;

public interface FileSystemIoDriver
{
    InputStream getInputStream(Path path, Set<OpenOption> options)
        throws IOException;

    default OutputStream getOutputStream(final Path path,
        final Set<OpenOption> options)
        throws IOException
    {
        throw new ReadOnlyFileSystemException();
    }

    default void createDirectory(final Path path)
        throws IOException
    {
        throw new ReadOnlyFileSystemException();
    }

    default void delete(final Path path)
        throws IOException
    {
        throw new ReadOnlyFileSystemException();
    }

    default void copy(final Path source, final Path target,
        final Set<CopyOption> options)
        throws IOException
    {
        throw new ReadOnlyFileSystemException();
    }

    default void move(final Path source, final Path target,
        final Set<CopyOption> options)
        throws IOException
    {
        copy(source, target, options);
        delete(source);
    }
}
