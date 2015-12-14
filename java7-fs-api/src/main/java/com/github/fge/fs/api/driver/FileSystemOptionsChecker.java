package com.github.fge.fs.api.driver;

import java.nio.file.CopyOption;
import java.nio.file.OpenOption;
import java.util.Set;

public interface FileSystemOptionsChecker
{
    Set<OpenOption> checkReadOptions(OpenOption... options);

    Set<OpenOption> checkWriteOptions(OpenOption... options);

    Set<CopyOption> checkCopyOptions(CopyOption... options);
}
