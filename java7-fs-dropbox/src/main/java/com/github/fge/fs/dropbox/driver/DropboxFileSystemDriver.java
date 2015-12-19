package com.github.fge.fs.dropbox.driver;

import com.dropbox.core.v2.DbxFiles;
import com.github.fge.fs.api.driver.DefaultFileSystemOptionsChecker;
import com.github.fge.fs.api.driver.FileSystemDriver;
import com.github.fge.fs.api.driver.FileSystemIoDriver;
import com.github.fge.fs.api.driver.FileSystemOptionsChecker;

public final class DropboxFileSystemDriver
    implements FileSystemDriver
{
    private final DbxFiles files;

    public DropboxFileSystemDriver(final DbxFiles files)
    {
        this.files = files;
    }

    @Override
    public FileSystemIoDriver getIoDriver()
    {
        return new DropboxFileSystemIoDriver(files);
    }

    @Override
    public FileSystemOptionsChecker getOptionsChecker()
    {
        return new DefaultFileSystemOptionsChecker();
    }
}
