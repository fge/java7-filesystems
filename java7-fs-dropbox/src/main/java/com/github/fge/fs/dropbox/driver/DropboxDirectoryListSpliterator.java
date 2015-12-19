package com.github.fge.fs.dropbox.driver;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxFiles;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public final class DropboxDirectoryListSpliterator
    implements Spliterator<String>
{
    private final DbxFiles files;
    private final String cursor;

    private DbxFiles.ListFolderResult result;
    private boolean hasMore;
    private List<DbxFiles.Metadata> entries;
    private int entriesSize;
    private int entriesIndex;

    public DropboxDirectoryListSpliterator(final DbxFiles files,
        final DbxFiles.ListFolderResult result)
    {
        this.files = files;
        this.result = result;
        cursor = result.cursor;

        hasMore = result.hasMore;
        entries = result.entries;
        entriesSize = entries.size();
        entriesIndex = 0;
    }

    @Override
    public boolean tryAdvance(final Consumer<? super String> action)
    {
        if (entriesIndex == entriesSize) {
            if (!hasMore)
                return false;
            try {
                result = files.listFolderContinue(cursor);
            } catch (DbxException e) {
                throw new RuntimeException(e);
            }
            hasMore = result.hasMore;
            entries = result.entries;
            entriesSize = entries.size();
            entriesIndex = 0;
        }

        action.accept(entries.get(entriesIndex++).name);
        return true;
    }

    @Override
    public Spliterator<String> trySplit()
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
        return DISTINCT | ORDERED | IMMUTABLE | NONNULL;
    }
}
