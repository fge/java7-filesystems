package com.github.fge.fs.dropbox.fs;

import com.github.fge.fs.api.driver.FileSystemDriver;
import com.github.fge.fs.api.filestore.AbstractFileStore;
import com.github.fge.fs.api.fs.AbstractFileSystem;
import com.github.fge.fs.api.path.PathContext;
import com.github.fge.fs.api.path.elements.PathElements;
import com.github.fge.fs.api.provider.AbstractFileSystemProvider;
import com.github.fge.fs.dropbox.path.DropboxPath;

import java.nio.file.Path;

public final class DropboxFileSystem
    extends AbstractFileSystem
{
    public DropboxFileSystem(final AbstractFileSystemProvider provider,
        final AbstractFileStore fileStore, final PathContext pathContext,
        final FileSystemDriver driver)
    {
        super(provider, fileStore, pathContext, driver);
    }

    @Override
    public Path buildPath(final PathElements elements)
    {
        return new DropboxPath(this, elements, pathContext);
    }
}
