package com.github.fge.fs.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxFiles;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;

public final class DropboxSample
{
    private static final String ACCESS_TOKEN_KEY = "accessToken";

    public static void main(final String... args)
        throws IOException, DbxException
    {
        final Path path = Paths.get(System.getProperty("user.home"),
            ".jsr203", "dropbox.properties");

        final Properties properties = new Properties();

        try (
            final Reader reader = Files.newBufferedReader(path);
        ) {
            properties.load(reader);
        }

        final String accessToken = properties.getProperty(ACCESS_TOKEN_KEY);

        if (accessToken == null)
            throw new IllegalStateException();

        final DbxRequestConfig cfg = new DbxRequestConfig("java7-fs-dropbox",
            Locale.US.toString());
        final DbxClientV2 client = new DbxClientV2(cfg, accessToken);

        final DbxFiles files = client.files;

        final DbxFiles.ListFolderResult result = files.listFolder("");

        for (final DbxFiles.Metadata metadata: result.entries)
            System.out.println(metadata.name);

        /*
         * It appears that in this metadata, as JSON, an object is returned and
         * that whether it is a file or a directory is identified by the ".tag"
         * object member (values: "file", "folder").
         */
        System.out.println(files.getMetadata("/resume.pdf").toJson(true));
    }
}
