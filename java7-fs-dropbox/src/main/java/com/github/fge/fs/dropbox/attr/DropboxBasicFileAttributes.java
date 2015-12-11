package com.github.fge.fs.dropbox.attr;

import com.dropbox.core.DbxEntry;
import com.github.fge.fs.api.attr.attributes.AbstractBasicFileAttributes;

import java.nio.file.attribute.FileTime;

public final class DropboxBasicFileAttributes
    extends AbstractBasicFileAttributes
{
    private final DbxEntry dbxEntry;

    public DropboxBasicFileAttributes(final DbxEntry entry)
    {
        dbxEntry = entry;
    }

    @Override
    public boolean isRegularFile()
    {
        return dbxEntry.isFile();
    }

    @Override
    public boolean isDirectory()
    {
        return dbxEntry.isFolder();
    }

    @Override
    public long size()
    {
        return isDirectory() ? -1L : dbxEntry.asFile().numBytes;
    }

    @Override
    public FileTime lastModifiedTime()
    {
        return isDirectory() ? EPOCH
            : FileTime.from(dbxEntry.asFile().lastModified.toInstant());
     }
}
