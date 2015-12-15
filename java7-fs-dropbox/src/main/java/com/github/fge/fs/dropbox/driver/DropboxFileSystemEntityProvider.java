package com.github.fge.fs.dropbox.driver;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.github.fge.fs.api.driver.FileSystemEntity;
import com.github.fge.fs.api.driver.FileSystemEntityProvider;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public final class DropboxFileSystemEntityProvider
    implements FileSystemEntityProvider
{
    private final DbxClient dbxClient;

    public DropboxFileSystemEntityProvider(final DbxClient dbxClient)
    {
        this.dbxClient = dbxClient;
    }

    @Override
    public FileSystemEntity getEntity(final Path path)
        throws IOException
    {
        final String pathname = path.toAbsolutePath().toString();
        try {
            final DbxEntry entry = dbxClient.getMetadata(pathname);
            if (entry == null)
                throw new NoSuchFileException(pathname);
            return new DropboxFileSystemEntity(entry);
        } catch (DbxException e) {
            throw new IOException(e);
        }
    }
}
