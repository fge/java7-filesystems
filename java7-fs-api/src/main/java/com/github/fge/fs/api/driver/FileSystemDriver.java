package com.github.fge.fs.api.driver;

public interface FileSystemDriver
    extends AutoCloseable
{
    FileSystemIoDriver getIoDriver();

    FileSystemOptionsChecker getOptionsChecker();
}
