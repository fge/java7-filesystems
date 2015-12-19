package com.github.fge.fs.dropbox.driver;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.v2.DbxFiles;

import java.io.IOException;
import java.io.InputStream;

public final class DropboxInputStream
    extends InputStream
{
    private final DbxDownloader<DbxFiles.FileMetadata> downloader;
    private final InputStream in;

    public DropboxInputStream(
        final DbxDownloader<DbxFiles.FileMetadata> downloader)
    {
        this.downloader = downloader;
        in = downloader.body;
    }

    @Override
    public int read()
        throws IOException
    {
        return in.read();
    }

    @Override
    public int read(final byte[] b)
        throws IOException
    {
        return in.read(b);
    }

    @Override
    public int read(final byte[] b, final int off, final int len)
        throws IOException
    {
        return in.read(b, off, len);
    }

    @Override
    public long skip(final long n)
        throws IOException
    {
        return in.skip(n);
    }

    @Override
    public int available()
        throws IOException
    {
        return in.available();
    }

    @Override
    public void close()
        throws IOException
    {
        IOException exception = null;
        try {
            in.close();
        } catch (IOException e) {
            exception = e;
        }

        downloader.close();

        if (exception != null)
            throw exception;
    }

    @Override
    public synchronized void mark(final int readlimit)
    {
        in.mark(readlimit);
    }

    @Override
    public synchronized void reset()
        throws IOException
    {
        in.reset();
    }

    @Override
    public boolean markSupported()
    {
        return in.markSupported();
    }
}
