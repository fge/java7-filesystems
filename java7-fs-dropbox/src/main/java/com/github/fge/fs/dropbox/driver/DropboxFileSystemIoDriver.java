package com.github.fge.fs.dropbox.driver;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxFiles;
import com.github.fge.fs.api.directory.DefaultDirectoryStream;
import com.github.fge.fs.api.driver.ReadOnlyFileSystemIoDriver;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.DirectoryStream;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class DropboxFileSystemIoDriver
    extends ReadOnlyFileSystemIoDriver
{
    private final DbxFiles files;

    public DropboxFileSystemIoDriver(final DbxFiles files)
    {
        this.files = files;
    }

    @Override
    public InputStream getInputStream(final Path path,
        final Set<OpenOption> options)
        throws IOException
    {
        final String dbxPath = toDropboxPath(path);
        final DbxDownloader<DbxFiles.FileMetadata> start;
        try {
            start = files.downloadBuilder(dbxPath).start();
        } catch (DbxFiles.DownloadException e) {
            IOException exception = null;
            if (e.errorValue.tag == DbxFiles.DownloadError.Tag.path) {
                switch (e.errorValue.getPath().tag) {
                    case notFile:
                        exception = new IOException(dbxPath
                            + " is not a regular file");
                        break;
                    case notFound:
                        exception = new NoSuchFileException(dbxPath);
                        break;
                    case restrictedContent:
                        exception = new AccessDeniedException(dbxPath);
                        // no other tags are interesting
                }
            }
            if (exception != null)
                exception.addSuppressed(e);
            else
                exception = new IOException(e);
            throw exception;
        } catch (DbxException e) {
            throw new IOException(e);
        }
        start.close();
        // TODO
        return null;
    }

    @Override
    public void checkAccess(final Path path, final AccessMode... modes)
        throws IOException
    {
        final String dbxPath = toDropboxPath(path);
        final DbxFiles.Metadata metadata;
        try {
            metadata = files.getMetadata(dbxPath);
        } catch (DbxFiles.GetMetadataException e) {
            IOException exception = null;
            switch (e.errorValue.getPath().tag) {
                case notFound:
                    exception = new NoSuchFileException(dbxPath);
                    break;
                case restrictedContent:
                    exception = new AccessDeniedException(dbxPath);
                    // no other tag is interesting
            }
            if (exception != null)
                exception.addSuppressed(e);
            else
                exception = new IOException(e);
            throw exception;
        } catch (DbxException e) {
            throw new IOException(e);
        }

        if (metadata instanceof DbxFiles.FileMetadata
            && Arrays.asList(modes).contains(AccessMode.EXECUTE))
            throw new AccessDeniedException(dbxPath);
    }

    @Override
    public DirectoryStream<Path> getDirectoryStream(final Path dir,
        final DirectoryStream.Filter<? super Path> filter)
        throws IOException
    {
        final String dbxPath = toDropboxPath(dir);
        // All of those are the defaults
        final DbxFiles.ListFolderBuilder builder = files
            .listFolderBuilder(dbxPath)
            .includeDeleted(false)
            .includeMediaInfo(false)
            .recursive(false);
        final DbxFiles.ListFolderResult result;
        try {
            result = builder.start();
        } catch (DbxFiles.ListFolderException e) {
            final DbxFiles.ListFolderError.Tag tag = e.errorValue.tag;
            IOException exception = null;
            if (tag == DbxFiles.ListFolderError.Tag.path) {
                switch (e.errorValue.getPath().tag) {
                    case notFolder:
                        exception = new NotDirectoryException(dbxPath);
                        break;
                    case notFound:
                        exception = new NoSuchFileException(dbxPath);
                        break;
                    case restrictedContent:
                        exception = new AccessDeniedException(dbxPath);
                        // no other tag is interesting
                }
            }
            if (exception == null)
                throw new IOException(e);
            exception.addSuppressed(e);
            throw exception;
        } catch (DbxException e) {
            throw new IOException(e);
        }

        final Spliterator<String> spliterator
            = new DropboxDirectoryListSpliterator(files, result);
        final Stream<Path> stream = StreamSupport.stream(spliterator, false)
            .map(dir::resolve);
        return new DefaultDirectoryStream(stream, filter);
    }

    private static String toDropboxPath(final Path path)
    {
        return path.toAbsolutePath().toString();
    }
}
