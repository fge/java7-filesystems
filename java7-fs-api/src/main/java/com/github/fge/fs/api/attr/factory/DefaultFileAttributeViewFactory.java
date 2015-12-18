package com.github.fge.fs.api.attr.factory;

import com.github.fge.fs.api.attr.StandardAttributeViewNames;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class DefaultFileAttributeViewFactory
    implements FileAttributeViewFactory
{
    private final Map<String, Class<?>> nameMap;
    private final Map<Class<?>, AttributeViewProvider<?>> viewProviders;
    private final Map<Class<?>, AttributesProvider<?>> attributesProviders;

    public static Builder builder()
    {
        return new Builder();
    }

    private DefaultFileAttributeViewFactory(final Builder builder)
    {
        nameMap = builder.nameMap;
        viewProviders = builder.viewProviders;
        attributesProviders = builder.attributesProviders;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends FileAttributeView> AttributeViewProvider<V>
        getProviderForName(final String name)
    {
        final Class<V> viewClass = (Class<V>) nameMap.get(name);
        return viewClass == null ? null : getProviderForClass(viewClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends FileAttributeView> AttributeViewProvider<V>
        getProviderForClass(final Class<V> viewClass)
    {
        for (final Map.Entry<Class<?>, AttributeViewProvider<?>> entry:
            viewProviders.entrySet())
            if (viewClass.isAssignableFrom(entry.getKey()))
                return (AttributeViewProvider<V>) entry.getValue();

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A extends BasicFileAttributes> AttributesProvider<A>
    getAttributesProvider(final Class<A> attributesClass)
    {
        for (final Map.Entry<Class<?>, AttributesProvider<?>> entry:
            attributesProviders.entrySet())
            if (attributesClass.isAssignableFrom(entry.getKey()))
                return (AttributesProvider<A>) entry.getValue();

        return null;
    }

    public static final class Builder
    {
        private final Map<String, Class<?>> nameMap
            = new HashMap<>(StandardAttributeViewNames.getStandardMap());
        private final Map<Class<?>, AttributeViewProvider<?>> viewProviders
            = new HashMap<>();
        private final Map<Class<?>, AttributesProvider<?>> attributesProviders
            = new HashMap<>();

        private Builder()
        {
        }

        public <V extends FileAttributeView> Builder addViewProvider(
            final AttributeViewProvider<V> provider)
        {
            Objects.requireNonNull(provider);

            final String name = Objects.requireNonNull(provider.getName());
            final Class<V> viewClass = Objects.requireNonNull(
                provider.getViewClass());

            if (nameMap.containsKey(name))
                throw new IllegalArgumentException();
            if (viewProviders.put(viewClass, provider) != null)
                throw new IllegalArgumentException();
            return this;
        }

        public <A extends BasicFileAttributes> Builder addAttributesProvider(
            final Class<A> attributesclass, final AttributesProvider<A> provider
        )
        {
            Objects.requireNonNull(attributesclass);
            Objects.requireNonNull(provider);

            if (attributesProviders.put(attributesclass, provider) != null)
                throw new IllegalArgumentException();
            return this;
        }

        public FileAttributeViewFactory build()
        {
            return new DefaultFileAttributeViewFactory(this);
        }
    }
}
