package com.github.fge.fs.dropbox.driver;

import com.dropbox.core.DbxEntry;
import com.github.fge.fs.api.driver.FileSystemEntity;

import java.nio.file.AccessMode;
import java.nio.file.Path;

public final class DropboxFileSystemEntity
    extends FileSystemEntity
{
    private final DbxEntry entry;

    public DropboxFileSystemEntity(final Path path, final DbxEntry entry)
    {
        super(path);
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
