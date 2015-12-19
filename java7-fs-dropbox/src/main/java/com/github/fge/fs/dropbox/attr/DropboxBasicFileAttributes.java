package com.github.fge.fs.dropbox.attr;

import com.dropbox.core.DbxEntry;
import com.github.fge.fs.api.attr.attributes.AbstractBasicFileAttributes;

import java.nio.file.attribute.FileTime;

public final class DropboxBasicFileAttributes
    extends AbstractBasicFileAttributes
{
    private final DbxEntry entry;

    public DropboxBasicFileAttributes(final DbxEntry entry)
    {
        this.entry = entry;
    }

    @Override
    public boolean isRegularFile()
    {
        return entry.isFile();
    }

    @Override
    public boolean isDirectory()
    {
        return entry.isFolder();
    }

    @Override
    public long size()
    {
        return isDirectory() ? -1L : entry.asFile().numBytes;
    }

    @Override
    public FileTime lastModifiedTime()
    {
        return isDirectory() ? EPOCH
            : FileTime.from(entry.asFile().lastModified.toInstant());
    }
}
