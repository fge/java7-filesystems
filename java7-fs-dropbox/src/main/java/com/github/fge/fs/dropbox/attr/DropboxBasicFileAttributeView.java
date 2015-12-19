package com.github.fge.fs.dropbox.attr;

import com.dropbox.core.v2.DbxFiles;
import com.github.fge.fs.api.attr.views.BasicFileAttributeViewBase;

import java.nio.file.attribute.BasicFileAttributes;

public final class DropboxBasicFileAttributeView
    implements BasicFileAttributeViewBase
{
    private final DropboxBasicFileAttributes attributes;

    public DropboxBasicFileAttributeView(final DbxFiles.Metadata metadata)
    {
        attributes = new DropboxBasicFileAttributes(metadata);
    }

    @Override
    public BasicFileAttributes readAttributes()
    {
        return attributes;
    }
}
