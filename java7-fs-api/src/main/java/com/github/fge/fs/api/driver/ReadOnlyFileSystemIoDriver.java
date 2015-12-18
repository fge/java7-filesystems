package com.github.fge.fs.api.driver;

import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.ReadOnlyFileSystemException;
import java.util.Set;

public abstract class ReadOnlyFileSystemIoDriver
    extends FileSystemIoDriver
{
    @Override
    public final OutputStream getOutputStream(final Path path,
        final Set<OpenOption> options)
    {
        throw new ReadOnlyFileSystemException();
    }

    @Override
    public final void createDirectory(final Path path)
    {
        throw new ReadOnlyFileSystemException();
    }

    @Override
    public final void delete(final Path path)
    {
        throw new ReadOnlyFileSystemException();
    }

    @Override
    public final void copy(final Path source, final Path target,
        final Set<CopyOption> options)
    {
        throw new ReadOnlyFileSystemException();
    }

    @Override
    public final void move(final Path source, final Path target,
        final Set<CopyOption> options)
    {
        throw new ReadOnlyFileSystemException();
    }
}
