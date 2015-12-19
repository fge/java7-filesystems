package com.github.fge.fs.dropbox.driver;

import com.dropbox.core.v1.DbxClientV1;

import java.io.IOException;
import java.io.InputStream;

public final class DropboxInputStream
    extends InputStream
{
    private final DbxClientV1.Downloader downloader;
    private final InputStream delegate;

    public DropboxInputStream(final DbxClientV1.Downloader downloader)
    {
        this.downloader = downloader;
        delegate = downloader.body;
    }

    @Override
    public int read()
        throws IOException
    {
        return delegate.read();
    }

    @Override
    public int read(final byte[] b)
        throws IOException
    {
        return delegate.read(b);
    }

    @Override
    public int read(final byte[] b, final int off, final int len)
        throws IOException
    {
        return delegate.read(b, off, len);
    }

    @Override
    public long skip(final long n)
        throws IOException
    {
        return delegate.skip(n);
    }

    @Override
    public int available()
        throws IOException
    {
        return delegate.available();
    }

    @Override
    public synchronized void mark(final int readlimit)
    {
        delegate.mark(readlimit);
    }

    @Override
    public boolean markSupported()
    {
        return delegate.markSupported();
    }

    @Override
    public synchronized void reset()
        throws IOException
    {
        delegate.reset();
    }

    @Override
    public void close()
        throws IOException
    {
        IOException exception = null;

        try {
            delegate.close();
        } catch (IOException e) {
            exception = e;
        }

        downloader.close();

        if (exception != null)
            throw exception;
    }
}
