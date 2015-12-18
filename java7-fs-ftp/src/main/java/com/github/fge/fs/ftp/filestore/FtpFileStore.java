package com.github.fge.fs.ftp.filestore;

import com.github.fge.fs.api.filestore.AbstractReadOnlyFileStore;

public final class FtpFileStore
    extends AbstractReadOnlyFileStore
{
    public FtpFileStore()
    {
        super("ftp");
    }
}
