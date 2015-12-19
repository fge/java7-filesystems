package com.github.fge.fs.dropbox.attr;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxFiles;
import com.github.fge.fs.api.attr.load.FileAttributeViewLoader;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;

public final class DropboxBasicFileAttributeViewLoader
    implements FileAttributeViewLoader<BasicFileAttributeView>
{
    private final DbxFiles files;

    public DropboxBasicFileAttributeViewLoader(final DbxFiles files)
    {
        this.files = files;
    }

    @Override
    public BasicFileAttributeView load(final Path path)
        throws DbxException
    {
        final String name = path.toAbsolutePath().toString();
        final DbxFiles.Metadata metadata
            = files.getMetadata(path.toAbsolutePath().toString());
        return new DropboxBasicFileAttributeView(metadata);
    }
}
