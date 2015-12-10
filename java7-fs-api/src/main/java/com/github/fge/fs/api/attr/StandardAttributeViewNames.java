package com.github.fge.fs.api.attr;

import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.UserDefinedFileAttributeView;

/**
 * List of standard file attribute view names defined by the JDK
 *
 * @see FileAttributeView#name()
 */
public final class StandardAttributeViewNames
{
    private StandardAttributeViewNames()
    {
        throw new Error("instantiation not permitted");
    }

    /**
     * Name of an {@link AclFileAttributeView}
     */
    public static final String ACL = "acl";

    /**
     * Name of a {@link BasicFileAttributeView}
     */
    public static final String BASIC = "basic";

    /**
     * Name of a {@link DosFileAttributeView}
     */
    public static final String DOS = "dos";

    /**
     * Name of a {@link FileOwnerAttributeView}
     */
    public static final String OWNER = "owner";

    /**
     * Name of a {@link PosixFileAttributeView}
     */
    public static final String POSIX = "posix";

    /**
     * Name of a {@link UserDefinedFileAttributeView}
     */
    public static final String USER = "user";
}
