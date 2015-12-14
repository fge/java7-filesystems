package com.github.fge.fs.api.driver;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Set;

public interface FileSystemIoDriver
{
    InputStream getInputStream(Path path, Set<OpenOption> options);

    OutputStream getOutputStream(Path path, Set<OpenOption> options);
}
