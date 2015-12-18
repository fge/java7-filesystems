package com.github.fge.fs.api.attr.factory;

import com.github.fge.fs.api.attr.load.FileAttributeViewLoader;
import com.github.fge.fs.api.attr.load.FileAttributesReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiFunction;

public abstract class AbstractAttributesProvider<V extends BasicFileAttributeView, A extends BasicFileAttributes>
    implements AttributesProvider<A>
{
    private final Class<A> attributesClass;
    private final FileAttributeViewLoader<V> loader;
    private final BiFunction<FileAttributeViewLoader<V>, Path,
        FileAttributesReader<A>> lazyLoader;

    protected AbstractAttributesProvider(final Class<A> attributesClass,
        final FileAttributeViewLoader<V> loader,
        final BiFunction<FileAttributeViewLoader<V>, Path,
            FileAttributesReader<A>> lazyLoader)
    {
        this.attributesClass = attributesClass;
        this.loader = loader;
        this.lazyLoader = lazyLoader;
    }

    @Override
    public Class<A> getAttributesClass()
    {
        return attributesClass;
    }

    @Override
    public A getAttributes(final Path path)
        throws IOException
    {
        return lazyLoader.apply(loader, path).readAttributes();
    }
}
