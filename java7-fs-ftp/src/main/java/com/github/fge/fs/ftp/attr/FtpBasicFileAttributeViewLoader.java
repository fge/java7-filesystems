package com.github.fge.fs.ftp.attr;

import com.github.fge.fs.api.attr.load.FileAttributeViewLoader;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;

public final class FtpBasicFileAttributeViewLoader
    implements FileAttributeViewLoader<BasicFileAttributeView>
{
    private final FTPClient ftpClient;

    public FtpBasicFileAttributeViewLoader(final FTPClient ftpClient)
    {
        this.ftpClient = ftpClient;
    }

    @Override
    public BasicFileAttributeView load(final Path path)
        throws IOException
    {
        final String name = path.toAbsolutePath().toString();
        final FTPFile ftpFile = ftpClient.mlistFile(name);
        return new FtpBasicFileAttributeView(ftpFile);
    }
}
