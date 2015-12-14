package com.github.fge.fs.api.driver;

public interface FileSystemDriver
{
    FileSystemIoDriver getIoDriver();

    FileSystemOptionsChecker getOptionsChecker();
}
