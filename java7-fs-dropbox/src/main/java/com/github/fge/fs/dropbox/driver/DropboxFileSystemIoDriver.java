package com.github.fge.fs.dropbox.driver;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;
import com.github.fge.fs.api.driver.FileSystemIoDriver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Set;

public final class DropboxFileSystemIoDriver
    implements FileSystemIoDriver
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
        final DbxClient.Downloader downloader;
        try {
            downloader = dbxClient.startGetFile(name, null);
        } catch (DbxException e) {
            throw new IOException(e);
        }
        return new DropboxInputStream(downloader);
    }

    @Override
    public OutputStream getOutputStream(final Path path,
        final Set<OpenOption> options)
        throws IOException
    {
        final String name = path.toAbsolutePath().toString();
        final boolean createNew = options
            .contains(StandardOpenOption.CREATE_NEW);

        try {
            if (createNew && dbxClient.getMetadata(name) != null)
                throw new FileAlreadyExistsException(name);
            final DbxClient.Uploader uploader
                = dbxClient.startUploadFile(name, DbxWriteMode.force(), -1);
            return new DropboxOutputStream(uploader);
        } catch (DbxException e) {
            throw new IOException(e);
        }
    }

    // TODO: 403 not handled, among other things
    @Override
    public void createDirectory(final Path path)
        throws IOException
    {
        final String name = path.toAbsolutePath().toString();
        try {
            dbxClient.createFolder(name);
        } catch (DbxException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void delete(final Path path)
        throws IOException
    {
        final String name = path.toAbsolutePath().toString();
        try {
            // TODO: check for directory; this API command would delete it
            dbxClient.delete(name);
        } catch (DbxException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void copy(final Path source, final Path target,
        final Set<CopyOption> options)
        throws IOException
    {
        final String srcname = source.toAbsolutePath().toString();
        final String dstname = target.toAbsolutePath().toString();

        if (options.contains(StandardCopyOption.REPLACE_EXISTING)) {
            // TODO: that should be the code for delete
            // TODO: check that the destination is a file or empty dir
            try {
                if (dbxClient.getMetadata(dstname) != null)
                    dbxClient.delete(dstname);
            } catch (DbxException e) {
                throw new IOException(e);
            }
        }

        try {
            // TODO: check that src is a file or empty dir
            dbxClient.copy(srcname, dstname);
        } catch (DbxException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void move(final Path source, final Path target,
        final Set<CopyOption> options)
        throws IOException
    {
        final String srcname = source.toAbsolutePath().toString();
        final String dstname = target.toAbsolutePath().toString();

        try {
            dbxClient.move(srcname, dstname);
        } catch (DbxException e) {
            throw new IOException(e);
        }
    }
}
