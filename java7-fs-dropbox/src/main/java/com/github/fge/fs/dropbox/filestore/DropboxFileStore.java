package com.github.fge.fs.dropbox.filestore;

import com.github.fge.fs.api.attr.factory.FileAttributeViewFactory;
import com.github.fge.fs.api.filestore.AbstractFileStore;

public final class DropboxFileStore
    extends AbstractFileStore
{
    private static final String DROPBOX_FILESTORE_NAME = "dropbox";

    public DropboxFileStore(
        final FileAttributeViewFactory fileAttributeViewFactory)
    {
        super(DROPBOX_FILESTORE_NAME, fileAttributeViewFactory);
    }
}
