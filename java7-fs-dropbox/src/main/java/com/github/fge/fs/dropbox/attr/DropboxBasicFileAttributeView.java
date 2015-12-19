package com.github.fge.fs.dropbox.attr;

import com.dropbox.core.v1.DbxEntry;
import com.github.fge.fs.api.attr.views.BasicFileAttributeViewBase;

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
    {
        return attributes;
    }
}
