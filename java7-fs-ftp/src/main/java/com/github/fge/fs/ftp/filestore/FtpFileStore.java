package com.github.fge.fs.ftp.filestore;

import com.github.fge.fs.api.filestore.AbstractFileStore;

public final class FtpFileStore
    extends AbstractFileStore
{
    public FtpFileStore(final String name)
    {
        super("ftp");
    }
}
