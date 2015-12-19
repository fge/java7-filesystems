package com.github.fge.fs.dropbox.path;

import com.github.fge.fs.api.fs.AbstractFileSystem;
import com.github.fge.fs.api.path.AbstractPath;
import com.github.fge.fs.api.path.PathContext;
import com.github.fge.fs.api.path.elements.PathElements;

public final class DropboxPath
    extends AbstractPath
{
    public DropboxPath(final AbstractFileSystem fileSystem,
        final PathElements elements, final PathContext pathContext)
    {
        super(fileSystem, elements, pathContext);
    }
}
