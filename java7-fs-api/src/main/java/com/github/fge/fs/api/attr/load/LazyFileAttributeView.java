package com.github.fge.fs.api.attr.load;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttributeView;

public abstract class LazyFileAttributeView<V extends FileAttributeView>
{
    protected final FileAttributeViewLoader<V> loader;
    protected final Path path;

    protected LazyFileAttributeView(final FileAttributeViewLoader<V> loader,
        final Path path)
    {
        this.loader = loader;
        this.path = path;
    }

    protected final V loadView()
        throws IOException
    {
        try {
            return loader.load(path);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
