package com.github.fge.fs.dropbox.entity;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.github.fge.fs.api.entity.DirectoryFileSystemEntity;

import java.nio.file.AccessMode;
import java.nio.file.Path;

public final class DropboxDirectoryFileSystemEntity
    extends DropboxFileSystemEntity
    implements DirectoryFileSystemEntity
{
    public DropboxDirectoryFileSystemEntity(final Path path,
        final DbxClient dbxClient,
        final DbxEntry entry)
    {
        super(path, dbxClient, entry);
    }

    @Override
    public boolean hasAccess(final AccessMode... modes)
    {
        return true;
    }
}
