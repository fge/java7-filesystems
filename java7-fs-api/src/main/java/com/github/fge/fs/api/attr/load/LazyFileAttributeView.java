package com.github.fge.fs.api.attr.load;

import java.io.IOException;
import java.nio.file.ClosedFileSystemException;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttributeView;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class LazyFileAttributeView<V extends FileAttributeView>
{
    protected final ExecutorService executor;
    protected final Future<V> future;

    protected LazyFileAttributeView(final ExecutorService executor,
        final FileAttributeViewLoader<V> loader, final Path path)
    {
        this.executor = executor;
        future = executor.submit(() -> loader.load(path));
    }

    protected final V loadView()
        throws IOException
    {
        try {
            return future.get();
        } catch (InterruptedException | CancellationException ignored) {
            throw new ClosedFileSystemException();
        } catch (ExecutionException e) {
            final Throwable cause = e.getCause();
            throw cause instanceof IOException
                ? (IOException) cause
                : new IOException(cause);
        }
    }
}
