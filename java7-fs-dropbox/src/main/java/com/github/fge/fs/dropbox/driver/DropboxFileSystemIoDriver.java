package com.github.fge.fs.dropbox.driver;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;
import com.github.fge.fs.api.directory.DefaultDirectoryStream;
import com.github.fge.fs.api.driver.FileSystemEntity;
import com.github.fge.fs.api.driver.FileSystemIoDriver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.stream.Stream;

public final class DropboxFileSystemIoDriver
    extends FileSystemIoDriver
{
    private final DbxClient dbxClient;

    public DropboxFileSystemIoDriver(final DbxClient dbxClient)
    {
        super(new DropboxFileSystemEntityProvider(dbxClient));
        this.dbxClient = dbxClient;
    }

    @Override
    public InputStream getInputStream(final Path path,
        final Set<OpenOption> options)
        throws IOException
    {
        final FileSystemEntity entity = entityProvider.getEntity(path);
        final String name = entity.toString();

        switch (entity.getType()) {
            case ENOENT:
                throw new NoSuchFileException(name);
            case REGULAR_FILE:
                break;
            default:
                throw new IOException(name + " is not a regular file");
        }

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
        final FileSystemEntity entity = entityProvider.getEntity(path);
        final String name = entity.toString();

        switch (entity.getType()) {
            case ENOENT:
                if (!options.contains(StandardOpenOption.CREATE))
                    throw new NoSuchFileException(name);
                break;
            case REGULAR_FILE:
                if (options.contains(StandardOpenOption.CREATE_NEW))
                    throw new FileAlreadyExistsException(name);
                break;
            default:
                throw new IOException(name + " is not a regular file");
        }

        try {
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
        final FileSystemEntity entity = entityProvider.getEntity(path);
        final String name = entity.toString();

        if (entity.getType() != FileSystemEntity.Type.ENOENT)
            throw new FileAlreadyExistsException(name);

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
        final FileSystemEntity entity = entityProvider.getEntity(path);
        final String name = entity.toString();

        switch (entity.getType()) {
            case ENOENT:
                throw new NoSuchFileException(name);
            case DIRECTORY:
                // TODO: check for non empty directory
                break;
            case REGULAR_FILE:
                break;
        }
        try {
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
        final FileSystemEntity srcEntity = entityProvider.getEntity(source);
        final String srcname = srcEntity.toString();

        switch (srcEntity.getType()) {
            case ENOENT:
                throw new NoSuchFileException(srcname);
            case DIRECTORY:
                // TODO: check for non empty directory
                break;
            case REGULAR_FILE:
                break;
        }

        final FileSystemEntity dstEntity = entityProvider.getEntity(target);
        final String dstname = dstEntity.toString();

        switch (dstEntity.getType()) {
            case ENOENT:
                break;
            case DIRECTORY:
                // TODO: check for non empty directory
                // fall through
            case REGULAR_FILE:
                if (!options.contains(StandardCopyOption.REPLACE_EXISTING))
                    throw new FileAlreadyExistsException(dstname);
        }

        try {
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
        final FileSystemEntity srcEntity = entityProvider.getEntity(source);
        final String srcname = srcEntity.toString();

        switch (srcEntity.getType()) {
            case ENOENT:
                throw new NoSuchFileException(srcname);
            case DIRECTORY:
                // TODO: check for non empty directory
                break;
            case REGULAR_FILE:
                break;
        }

        final FileSystemEntity dstEntity = entityProvider.getEntity(target);
        final String dstname = dstEntity.toString();

        switch (dstEntity.getType()) {
            case ENOENT:
                break;
            case DIRECTORY:
                // TODO: check for non empty directory
                // fall through
            case REGULAR_FILE:
                if (!options.contains(StandardCopyOption.REPLACE_EXISTING))
                    throw new FileAlreadyExistsException(dstname);
        }

        try {
            dbxClient.move(srcname, dstname);
        } catch (DbxException e) {
            throw new IOException(e);
        }
    }

    @Override
    public DirectoryStream<Path> getDirectoryStream(final Path dir,
        final DirectoryStream.Filter<? super Path> filter)
        throws IOException
    {
        final FileSystemEntity entity = entityProvider.getEntity(dir);
        final String name = entity.toString();

        switch (entity.getType()) {
            case ENOENT:
                throw new NoSuchFileException(name);
            case DIRECTORY:
                break;
            default:
                throw new NotDirectoryException(name);
        }

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
