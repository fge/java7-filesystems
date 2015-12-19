package com.github.fge.fs.dropbox.attr;

import com.dropbox.core.v2.DbxFiles;
import com.github.fge.fs.api.attr.attributes.AbstractBasicFileAttributes;

import java.nio.file.attribute.FileTime;
import java.util.Date;

public final class DropboxBasicFileAttributes
    extends AbstractBasicFileAttributes
{
    private final DbxFiles.Metadata metadata;

    public DropboxBasicFileAttributes(final DbxFiles.Metadata metadata)
    {
        this.metadata = metadata;
    }

    @Override
    public boolean isRegularFile()
    {
        return metadata instanceof DbxFiles.FileMetadata;
    }

    @Override
    public boolean isDirectory()
    {
        return metadata instanceof DbxFiles.FolderMetadata;
    }

    @Override
    public long size()
    {
        return isDirectory() ? 0L : ((DbxFiles.FileMetadata) metadata).size;
    }

    @Override
    public FileTime lastModifiedTime()
    {
        if (isDirectory())
            return EPOCH;
        final Date modified = ((DbxFiles.FileMetadata) metadata).serverModified;
        return FileTime.from(modified.toInstant());
    }
}
