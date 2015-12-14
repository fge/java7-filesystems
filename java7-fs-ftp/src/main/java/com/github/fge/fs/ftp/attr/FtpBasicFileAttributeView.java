package com.github.fge.fs.ftp.attr;

import com.github.fge.fs.api.attr.views.BasicFileAttributeViewBase;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributes;

public final class FtpBasicFileAttributeView
    implements BasicFileAttributeViewBase
{
    private final FtpBasicFileAttributes attributes;

    public FtpBasicFileAttributeView(final FTPFile ftpFile)
    {
        attributes = new FtpBasicFileAttributes(ftpFile);
    }

    @Override
    public BasicFileAttributes readAttributes()
        throws IOException
    {
        return attributes;
    }
}
