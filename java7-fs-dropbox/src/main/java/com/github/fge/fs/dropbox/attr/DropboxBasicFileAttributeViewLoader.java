package com.github.fge.fs.dropbox.attr;

import com.dropbox.core.DbxException;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry;
import com.github.fge.fs.api.attr.load.FileAttributeViewLoader;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;

public final class DropboxBasicFileAttributeViewLoader
    implements FileAttributeViewLoader<BasicFileAttributeView>
{
    private final DbxClientV1 client;

    public DropboxBasicFileAttributeViewLoader(final DbxClientV1 client)
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
