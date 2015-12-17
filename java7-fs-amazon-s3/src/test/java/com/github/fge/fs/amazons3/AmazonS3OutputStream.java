package com.github.fge.fs.amazons3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class AmazonS3OutputStream
    extends OutputStream
{
    private static final ExecutorService EXECUTOR
        = Executors.newCachedThreadPool(r -> {
            final Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("upload-test");
            return thread;
        });

    private final AmazonS3 amazonS3;
    private final String bucket;
    private final String targetFile;
    private final Future<PutObjectResult> future;

    private final ObjectMetadata metadata = new ObjectMetadata();
    private final PipedInputStream in = new PipedInputStream();
    private final PipedOutputStream out = new PipedOutputStream();

    public AmazonS3OutputStream(final AmazonS3 s3, final String bucket,
        final String targetFile)
    {
        amazonS3 = s3;
        this.bucket = bucket;
        this.targetFile = targetFile;

        final Callable<PutObjectResult> callable = () -> {
            out.connect(in);
            return s3.putObject(bucket, targetFile, in, metadata);
        };

        future = EXECUTOR.submit(callable);
    }

    @Override
    public void write(final int b)
        throws IOException
    {
        out.write(b);
    }

    @Override
    public void write(final byte[] b, final int off, final int len)
        throws IOException
    {
        out.write(b, off, len);
    }

    @Override
    public void flush()
        throws IOException
    {
        out.flush();
    }

    @Override
    public void close()
        throws IOException
    {
        out.close();

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(final byte[] b)
        throws IOException
    {
        out.write(b);
    }
}
