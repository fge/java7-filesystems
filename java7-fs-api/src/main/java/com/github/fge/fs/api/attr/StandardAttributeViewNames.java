package com.github.fge.fs.api.attr;

import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    private static final Map<String, Class<? extends FileAttributeView>>
        STANDARD_MAP;

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

    static {
        final Map<String, Class<? extends FileAttributeView>> map
            = new HashMap<>();

        map.put(ACL, AclFileAttributeView.class);
        map.put(BASIC, BasicFileAttributeView.class);
        map.put(DOS, DosFileAttributeView.class);
        map.put(OWNER, FileOwnerAttributeView.class);
        map.put(POSIX, PosixFileAttributeView.class);
        map.put(USER, UserDefinedFileAttributeView.class);

        STANDARD_MAP = Collections.unmodifiableMap(map);
    }

    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public static Map<String, Class<? extends FileAttributeView>>
    getStandardMap()
    {
        return STANDARD_MAP;
    }
}
