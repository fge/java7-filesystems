package com.github.fge.fs.ftp.attr;

import com.github.fge.fs.api.attr.attributes.AbstractBasicFileAttributes;
import org.apache.commons.net.ftp.FTPFile;

import java.nio.file.attribute.FileTime;

public final class FtpBasicFileAttributes
    extends AbstractBasicFileAttributes
{
    private final FTPFile ftpFile;

    public FtpBasicFileAttributes(final FTPFile file)
    {
        ftpFile = file;
    }

    @Override
    public FileTime lastModifiedTime()
    {
        return FileTime.from(ftpFile.getTimestamp().toInstant());
    }

    @Override
    public boolean isRegularFile()
    {
        return ftpFile.isFile();
    }

    @Override
    public boolean isDirectory()
    {
        return ftpFile.isDirectory();
    }

    @Override
    public long size()
    {
        return ftpFile.getSize();
    }
}
