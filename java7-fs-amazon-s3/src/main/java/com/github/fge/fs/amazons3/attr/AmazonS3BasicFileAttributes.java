package com.github.fge.fs.amazons3.attr;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.github.fge.fs.api.attr.attributes.AbstractBasicFileAttributes;

import java.nio.file.attribute.FileTime;

public final class AmazonS3BasicFileAttributes
    extends AbstractBasicFileAttributes
{
    private final ObjectMetadata metadata;
    private final boolean directory;

    public AmazonS3BasicFileAttributes(final S3Object s3Object)
    {
        metadata = s3Object.getObjectMetadata();
        directory = s3Object.getKey().endsWith("/");
    }

    @Override
    public boolean isRegularFile()
    {
        return !directory;
    }

    @Override
    public boolean isDirectory()
    {
        return directory;
    }

    @Override
    public long size()
    {
        return metadata.getContentEncoding() != null ? 0
            : metadata.getContentLength();
    }

    @Override
    public FileTime lastModifiedTime()
    {
        return FileTime.from(metadata.getLastModified().toInstant());
    }
}
