package com.github.fge.fs.dropbox;

public final class DropboxFileSystemConstants
{
    private DropboxFileSystemConstants()
    {
        throw new Error("instantiation not permitted");
    }

    public static final String URI_SCHEME = "dropbox";
    public static final String FILESTORE_NAME = "dropbox";
    public static final String FILESTORE_TYPE = "dropbox";

    public static final String DROPBOX_PROPERTIES = "dropboxProperties";
    public static final String API_ACCESSTOKEN_KEY
        = "dropbox.api.accessToken";
    public static final String API_APPANE_KEY
        = "dropbbox.api.applicationName";
}
