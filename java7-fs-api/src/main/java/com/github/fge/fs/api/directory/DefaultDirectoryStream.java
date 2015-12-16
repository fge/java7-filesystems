package com.github.fge.fs.api.directory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.ClosedDirectoryStreamException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class DefaultDirectoryStream
    implements DirectoryStream<Path>
{
    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private final Stream<Path> stream;

    public DefaultDirectoryStream(final Stream<Path> stream,
        final Filter<? super Path> filter)
    {
        final Predicate<? super Path> predicate = path -> {
            try {
                return filter.accept(path);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };

        this.stream = stream.filter(predicate);
    }

    @Override
    public Iterator<Path> iterator()
    {
        if (isClosed.get())
            throw new ClosedDirectoryStreamException();
        return stream.iterator();
    }

    @Override
    public void close()
        throws IOException
    {
        if (!isClosed.getAndSet(true))
            stream.close();
    }
}
