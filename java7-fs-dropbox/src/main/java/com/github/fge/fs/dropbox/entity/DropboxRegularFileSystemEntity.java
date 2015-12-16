package com.github.fge.fs.dropbox.entity;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.github.fge.fs.api.entity.RegularFileSystemEntity;
import com.github.fge.fs.dropbox.driver.DropboxInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessMode;
import java.nio.file.Path;
import java.util.Arrays;

public final class DropboxRegularFileSystemEntity
    extends DropboxFileSystemEntity
    implements RegularFileSystemEntity
{
    public DropboxRegularFileSystemEntity(final Path path,
        final DbxClient dbxClient, final DbxEntry entry)
    {
        super(path, dbxClient, entry);
    }

    @Override
    public boolean hasAccess(final AccessMode... modes)
    {
        return Arrays.stream(modes)
            .noneMatch(mode -> mode != AccessMode.EXECUTE);
    }

    @Override
    public InputStream getInputStream()
        throws IOException
    {
        // TODO: permissions?
        final DbxClient.Downloader downloader;
        try {
            downloader = dbxClient.startGetFile(name, null);
        } catch (DbxException e) {
            throw new IOException(e);
        }
        return new DropboxInputStream(downloader);
    }
}
