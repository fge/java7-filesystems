package com.github.fge.fs.api.attr.attributes;

import java.nio.file.attribute.PosixFileAttributes;

/**
 * Abstract base implementation of {@link PosixFileAttributes}
 *
 * <p>This abstract class extends {@link AbstractBasicFileAttributes}; therefore
 * it inherits all of its default implementations as well.</p>
 */
// TODO: relationship with FileSystem's .getUserPrincipalLookupService()?
public abstract class AbstractPosixFileAttributes
    extends AbstractBasicFileAttributes
    implements PosixFileAttributes
{
}
