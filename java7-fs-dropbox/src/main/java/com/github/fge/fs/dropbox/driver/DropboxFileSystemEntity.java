package com.github.fge.fs.dropbox.driver;

import com.dropbox.core.DbxEntry;
import com.github.fge.fs.api.driver.FileSystemEntity;

import java.nio.file.AccessMode;

public final class DropboxFileSystemEntity
    implements FileSystemEntity
{
    private final DbxEntry entry;

    public DropboxFileSystemEntity(final DbxEntry entry)
    {
        this.entry = entry;
    }

    @Override
    public Type getType()
    {
        if (entry.isFile())
            return Type.REGULAR_FILE;
        if (entry.isFolder())
            return Type.DIRECTORY;
        // TODO: nothing else supposedly supported for Dropbox here, so...
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasAccess(final AccessMode... modes)
    {
        return true;
    }
}
