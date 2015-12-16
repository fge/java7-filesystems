package com.github.fge.fs.api.driver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Set;

public abstract class FileSystemIoDriver
{
    protected final FileSystemEntityProvider entityProvider;

    protected FileSystemIoDriver(final FileSystemEntityProvider provider)
    {
        entityProvider = provider;
    }

    public abstract InputStream getInputStream(Path path,
        Set<OpenOption> options)
        throws IOException;

    public abstract OutputStream getOutputStream(final Path path,
        final Set<OpenOption> options)
        throws IOException;

    public abstract void createDirectory(final Path path)
        throws IOException;

    public abstract void delete(final Path path)
        throws IOException;

    public abstract void copy(final Path source, final Path target,
        final Set<CopyOption> options)
        throws IOException;

    public void move(final Path source, final Path target,
        final Set<CopyOption> options)
        throws IOException
    {
        copy(source, target, options);
        delete(source);
    }
}
