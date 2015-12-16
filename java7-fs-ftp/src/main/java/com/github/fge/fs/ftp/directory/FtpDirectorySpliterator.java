package com.github.fge.fs.ftp.directory;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;

import java.nio.file.Path;
import java.util.Spliterator;
import java.util.function.Consumer;

public final class FtpDirectorySpliterator
    implements Spliterator<Path>
{
    private static final int NR_ENTRIES = 10;

    private final Path baseDir;
    private final FTPListParseEngine parseEngine;

    private FTPFile[] entries;
    private int entriesSize;
    private int entriesIndex;

    public FtpDirectorySpliterator(final Path dir,
        final FTPListParseEngine engine)
    {
        baseDir = dir;
        parseEngine = engine;
    }

    @Override
    public boolean tryAdvance(final Consumer<? super Path> action)
    {
        if (entries == null || entriesIndex == entriesSize) {
            entries = parseEngine.getNext(NR_ENTRIES);
            entriesSize = entries.length;
            entriesIndex = 0;
        }

        if (entriesSize == 0)
            return false;

        final FTPFile ftpFile = entries[entriesIndex++];
        final Path path = baseDir.resolve(ftpFile.getName());
        action.accept(path);
        return true;
    }

    @Override
    public Spliterator<Path> trySplit()
    {
        return null;
    }

    @Override
    public long estimateSize()
    {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics()
    {
        return DISTINCT | NONNULL | ORDERED | IMMUTABLE;
    }
}
