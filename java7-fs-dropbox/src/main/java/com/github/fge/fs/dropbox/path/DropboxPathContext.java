package com.github.fge.fs.dropbox.path;

import com.github.fge.fs.api.path.PathContext;
import com.github.fge.fs.api.path.elements.PathElements;
import com.github.fge.fs.api.path.elements.PathElementsFactory;
import com.github.fge.fs.api.path.elements.UnixPathElementsFactory;

import java.net.URI;

public final class DropboxPathContext
    implements PathContext
{
    private final PathElementsFactory factory = new UnixPathElementsFactory();
    private final URI baseUri;

    public DropboxPathContext(final URI uri)
    {
        baseUri = uri;
    }

    @Override
    public URI getBaseUri()
    {
        // TODO
        return null;
    }

    @Override
    public String getSeparator()
    {
        return "/";
    }

    @Override
    public PathElements getRootElements()
    {
        return factory.getFileSystemRoot();
    }

    @Override
    public PathElementsFactory getElementsFactory()
    {
        return factory;
    }
}
