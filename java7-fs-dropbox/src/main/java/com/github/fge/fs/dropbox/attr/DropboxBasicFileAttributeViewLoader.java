package com.github.fge.fs.dropbox.attr;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.github.fge.fs.api.attr.load.FileAttributeViewLoader;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;

public final class DropboxBasicFileAttributeViewLoader
    implements FileAttributeViewLoader<BasicFileAttributeView>
{
    private final DbxClient client;

    public DropboxBasicFileAttributeViewLoader(final DbxClient client)
    {
        this.client = client;
    }

    @Override
    public BasicFileAttributeView load(final Path path)
        throws DbxException
    {
        final String name = path.toAbsolutePath().toString();
        final DbxEntry entry = client.getMetadata(name);
        return new DropboxBasicFileAttributeView(entry);
    }
}
