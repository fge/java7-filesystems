package com.github.fge.fs.dropbox.attr;

import com.dropbox.core.DbxEntry;
import com.github.fge.fs.api.attr.views.BasicFileAttributeViewBase;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;

public final class DropboxBasicFileAttributeView
    implements BasicFileAttributeViewBase
{
    private final DropboxBasicFileAttributes attributes;

    public DropboxBasicFileAttributeView(final DbxEntry entry)
    {
        attributes = new DropboxBasicFileAttributes(entry);
    }

    @Override
    public BasicFileAttributes readAttributes()
        throws IOException
    {
        return attributes;
    }
}
