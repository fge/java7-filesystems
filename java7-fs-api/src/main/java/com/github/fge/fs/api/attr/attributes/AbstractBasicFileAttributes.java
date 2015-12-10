package com.github.fge.fs.api.attr.attributes;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

/**
 * Abstract default implementation of a {@link BasicFileAttributes} instance
 *
 * <p>Please see the {@code LIMITATIONS.md} file on the <a
 * href="https://github.com/fge/java7-filesystems">project page</a> for the
 * enforced limitation of this abstract implementation.</p>
 *
 * <p>Among other things, it means that the default implementation of {@link
 * #isSymbolicLink()} always returns false; until the API supports symbolic
 * links, and even though the implementation is not final, it is not advised
 * that you override it.</p>
 *
 * <p>Other default implementations are provided:</p>
 *
 * <ul>
 *     <li>all methods returning {@link FileTime} instances return a statically
 *     defined instance corresponding to Unix epoch: Jan 1st, 1970 at 00:00:00,
 *     GMT;</li>
 *     <li>{@link #isOther()} returns false;</li>
 *     <li>{@link #fileKey()} returns null.</li>
 * </ul>
 *
 * <p>The three methods returning file times are:</p>
 *
 * <ul>
 *     <li>{@link #lastAccessTime()};</li>
 *     <li>{@link #lastModifiedTime()};</li>
 *     <li>{@link #creationTime()}.</li>
 * </ul>
 */
public abstract class AbstractBasicFileAttributes
    implements BasicFileAttributes
{
    protected static final FileTime EPOCH = FileTime.fromMillis(0L);

    @Override
    public FileTime lastModifiedTime()
    {
        return EPOCH;
    }

    @Override
    public FileTime lastAccessTime()
    {
        return EPOCH;
    }

    @Override
    public FileTime creationTime()
    {
        return EPOCH;
    }

    @Override
    public boolean isSymbolicLink()
    {
        return false;
    }

    @Override
    public boolean isOther()
    {
        return false;
    }

    @Override
    public Object fileKey()
    {
        return null;
    }
}
