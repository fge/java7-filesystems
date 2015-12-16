package com.github.fge.fs.dropbox.entity;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.github.fge.fs.api.entity.AbstractFileSystemEntity;

import java.nio.file.Path;

public abstract class DropboxFileSystemEntity
    extends AbstractFileSystemEntity
{
    protected final DbxClient dbxClient;
    protected final DbxEntry entry;

    protected DropboxFileSystemEntity(final Path path,
        final DbxClient dbxClient, final DbxEntry entry)
    {
        super(path);
        this.dbxClient = dbxClient;
        this.entry = entry;
    }
}
