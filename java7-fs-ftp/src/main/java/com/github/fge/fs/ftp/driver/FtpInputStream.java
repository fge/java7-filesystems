package com.github.fge.fs.ftp.driver;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;

public final class FtpInputStream
    extends InputStream
{
    private final FTPClient client;
    private final InputStream in;

    public FtpInputStream(final FTPClient client, final InputStream in)
    {
        this.client = client;
        this.in = in;
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

        if (!client.completePendingCommand())
            exception = new IOException();

        try {
            in.close();
        } catch (IOException e) {
            if (exception == null)
                exception = e;
            else
                exception.addSuppressed(e);
        }

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
