package com.github.fge.fs.dropbox.filestore;

import com.github.fge.fs.api.attr.factory.FileAttributeViewFactory;
import com.github.fge.fs.api.filestore.AbstractFileStore;
import com.github.fge.fs.dropbox.DropboxFileSystemConstants;

public final class DropboxFileStore
    extends AbstractFileStore
{
    public DropboxFileStore(final FileAttributeViewFactory viewFactory)
    {
        super(DropboxFileSystemConstants.FILESTORE_NAME, viewFactory);
    }
}
