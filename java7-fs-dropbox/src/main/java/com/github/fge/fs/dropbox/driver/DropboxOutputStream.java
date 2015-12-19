package com.github.fge.fs.dropbox.driver;

import com.dropbox.core.v1.DbxClientV1;

import java.io.IOException;
import java.io.OutputStream;

public final class DropboxOutputStream
    extends OutputStream
{
    private final DbxClientV1.Uploader uploader;
    private final OutputStream delegate;

    public DropboxOutputStream(final DbxClientV1.Uploader uploader)
    {
        this.uploader = uploader;
        delegate = uploader.getBody();
    }

    @Override
    public void write(final int b)
        throws IOException
    {
        delegate.write(b);
    }

    @Override
    public void write(final byte[] b)
        throws IOException
    {
        delegate.write(b);
    }

    @Override
    public void write(final byte[] b, final int off, final int len)
        throws IOException
    {
        delegate.write(b, off, len);
    }

    @Override
    public void flush()
        throws IOException
    {
        delegate.flush();
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

        uploader.close();
        if (exception != null)
            throw exception;
    }
}
