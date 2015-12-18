package com.github.fge.fs.api.attr.factory;

import com.github.fge.fs.api.attr.StandardAttributeViewNames;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class FileAttributeViewFactory
{
    private final Map<String, Class<?>> nameMap;
    private final Map<Class<?>, AttributeViewProvider<?>> viewProviders;
    private final Map<Class<?>, AttributesProvider<?>> attributesProviders;

    private FileAttributeViewFactory(final Builder builder)
    {
        nameMap = builder.nameMap;
        viewProviders = builder.viewProviders;
        attributesProviders = builder.attributesProviders;
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
            return new FileAttributeViewFactory(this);
        }
    }
}
