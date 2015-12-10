package com.github.fge.fs.api.attr.attributes;

import java.nio.file.attribute.DosFileAttributes;

/**
 * Abstract base implementation of {@link DosFileAttributes}
 *
 * <p>This abstract class extends {@link AbstractBasicFileAttributes}; therefore
 * it inherits all of its default implementations as well.</p>
 */
public abstract class AbstractDosFileAttributes
    extends AbstractBasicFileAttributes
    implements DosFileAttributes
{
}
