package com.github.fge.fs.api.attr.views;

import com.github.fge.fs.api.attr.StandardAttributeViewNames;

import java.io.IOException;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.Set;

/**
 * Extension of {@link PosixFileAttributeView} with default implementations
 *
 * <p>The default implementations are as follows:</p>
 *
 * <ul>
 *     <li>{@link #name()} returns {@link StandardAttributeViewNames#POSIX};
 *     </li>
 *     <li>{@link #getOwner()} is implemented as:
 *     <pre>
 *         return readAttributes().owner();
 *     </pre>
 *     that is, it fetches the attributes first and calls the matching method;
 *     </li>
 *     <li>all methods setting attributes throw an {@link
 *     UnsupportedOperationException}; this includes all methods from both
 *     {@link BasicFileAttributeViewBase} and {@link FileOwnerAttributeViewBase}
 *     which this interface inherits.</li>
 * </ul>
 */
// TODO: relationship with FileSystem's .getUserPrincipalLookupService()?
@FunctionalInterface
public interface PosixFileAttributeViewBase
    extends PosixFileAttributeView, BasicFileAttributeViewBase,
    FileOwnerAttributeViewBase
{
    @Override
    default UserPrincipal getOwner()
        throws IOException
    {
        return readAttributes().owner();
    }

    @Override
    default String name()
    {
        return StandardAttributeViewNames.POSIX;
    }

    @Override
    default void setPermissions(final Set<PosixFilePermission> perms)
        throws IOException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    default void setGroup(final GroupPrincipal group)
        throws IOException
    {
        throw new UnsupportedOperationException();
    }
}
