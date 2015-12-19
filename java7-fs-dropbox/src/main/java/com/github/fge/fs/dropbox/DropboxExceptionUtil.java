package com.github.fge.fs.dropbox;

import com.dropbox.core.v2.DbxFiles;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;

public final class DropboxExceptionUtil
{
    private DropboxExceptionUtil()
    {
        throw new Error("instantiation not permitted");
    }

    public static IOException wrap(final String dbxPath,
        final DbxFiles.DownloadException e)
    {
        final DbxFiles.DownloadError error = e.errorValue;
        if (error.tag != DbxFiles.DownloadError.Tag.path)
            return new IOException(e);

        final IOException exception;

        switch (error.getPath().tag) {
            case notFile:
                exception = new IOException(dbxPath
                    + " is not a regular file");
                break;
            case notFound:
                exception = new NoSuchFileException(dbxPath);
                break;
            case restrictedContent:
                exception = new AccessDeniedException(dbxPath);
                break;
            default:
                return new IOException(e);
        }

        exception.addSuppressed(e);
        return exception;
    }

    public static IOException wrap(final String dbxPath,
        final DbxFiles.GetMetadataException e)
    {
        final IOException exception;

        switch (e.errorValue.getPath().tag) {
            case notFound:
                exception = new NoSuchFileException(dbxPath);
                break;
            case restrictedContent:
                exception = new AccessDeniedException(dbxPath);
                break;
            default:
                return new IOException(e);
        }

        exception.addSuppressed(e);
        return exception;
    }

    public static IOException wrap(final String dbxPath,
        final DbxFiles.ListFolderException e)
    {
        final DbxFiles.ListFolderError value = e.errorValue;

        if (value.tag != DbxFiles.ListFolderError.Tag.path)
            return new IOException(e);

        final IOException exception;

        switch (value.getPath().tag) {
            case notFolder:
                exception = new NotDirectoryException(dbxPath);
                break;
            case notFound:
                exception = new NoSuchFileException(dbxPath);
                break;
            case restrictedContent:
                exception = new AccessDeniedException(dbxPath);
                break;
            default:
                return new IOException(e);
        }

        exception.addSuppressed(e);
        return exception;
    }
}
