package com.github.fge.fs.api.attr.factory;

import com.github.fge.fs.api.attr.byname.NameDispatcher;
import com.github.fge.fs.api.attr.load.FileAttributeViewLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttributeView;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractAttributeViewProvider<V extends FileAttributeView>
    implements AttributeViewProvider<V>
{
    private final String name;
    private final Class<V> viewClass;
    private final FileAttributeViewLoader<V> loader;
    private final BiFunction<FileAttributeViewLoader<V>, Path, V> lazyLoader;
    private final Function<V, NameDispatcher> nameDispatcherFunction;

    protected AbstractAttributeViewProvider(final String name,
        final Class<V> viewClass, final FileAttributeViewLoader<V> loader,
        final BiFunction<FileAttributeViewLoader<V>, Path, V> lazyLoader,
        final Function<V, NameDispatcher> nameDispatcherFunction)
    {
        this.name = Objects.requireNonNull(name);
        this.viewClass = Objects.requireNonNull(viewClass);
        this.loader = Objects.requireNonNull(loader);
        this.lazyLoader = Objects.requireNonNull(lazyLoader);
        this.nameDispatcherFunction = Objects.requireNonNull(
            nameDispatcherFunction);
    }

    @Override
    public final String getName()
    {
        return name;
    }

    @Override
    public final Class<V> getViewClass()
    {
        return viewClass;
    }

    @Override
    public final V getView(final Path path)
    {
        return lazyLoader.apply(loader, path);
    }

    @Override
    public final NameDispatcher getNameDispatcher(final Path path)
        throws IOException
    {
        final V view;

        try {
            view = loader.load(path);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }

        return nameDispatcherFunction.apply(view);
    }
}
