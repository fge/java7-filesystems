package com.github.fge.fs.dropbox.driver;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.github.fge.fs.api.directory.DefaultDirectoryStream;
import com.github.fge.fs.api.driver.ReadOnlyFileSystemIoDriver;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessMode;
import java.nio.file.DirectoryStream;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

public final class DropboxFileSystemIoDriver
    extends ReadOnlyFileSystemIoDriver
{
    private final DbxClient dbxClient;

    public DropboxFileSystemIoDriver(final DbxClient dbxClient)
    {
        this.dbxClient = dbxClient;
    }

    @Override
    public InputStream getInputStream(final Path path,
        final Set<OpenOption> options)
        throws IOException
    {
        final String name = path.toAbsolutePath().toString();
        try {
            final DbxClient.Downloader downloader
                = dbxClient.startGetFile(name, null);
            return new DropboxInputStream(downloader);
        } catch (DbxException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void checkAccess(final Path path, final AccessMode... modes)
        throws IOException
    {
        // TODO

    }

    @Override
    public DirectoryStream<Path> getDirectoryStream(final Path dir,
        final DirectoryStream.Filter<? super Path> filter)
        throws IOException
    {
        final String name = dir.toAbsolutePath().toString();

        final DbxEntry.WithChildren children;
        try {
            children = dbxClient.getMetadataWithChildren(name);
        } catch (DbxException e) {
            throw new IOException(e);
        }

        final Stream<Path> stream = children.children.stream()
            .map(entry -> dir.resolve(entry.name));

        return new DefaultDirectoryStream(stream, filter);
    }
}
