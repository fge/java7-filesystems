package com.github.fge.fs.api.attr.factory;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;

public interface FileAttributeViewFactory
{
    @SuppressWarnings("unchecked")
    <V extends FileAttributeView> AttributeViewProvider<V>
        getProviderForName(String name);

    @SuppressWarnings("unchecked")
    <V extends FileAttributeView> AttributeViewProvider<V>
        getProviderForClass(Class<V> viewClass);

    @SuppressWarnings("unchecked")
    <A extends BasicFileAttributes> AttributesProvider<A>
        getAttributesProvider(Class<A> attributesClass);
}
