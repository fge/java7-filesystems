package com.github.fge.fs.dropbox.provider;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxFiles;
import com.github.fge.fs.api.attr.factory.AttributeViewProvider;
import com.github.fge.fs.api.attr.factory.AttributesProvider;
import com.github.fge.fs.api.attr.factory.BasicAttributeViewProvider;
import com.github.fge.fs.api.attr.factory.BasicFileAttributesProvider;
import com.github.fge.fs.api.attr.factory.DefaultFileAttributeViewFactory;
import com.github.fge.fs.api.attr.factory.FileAttributeViewFactory;
import com.github.fge.fs.api.attr.load.FileAttributeViewLoader;
import com.github.fge.fs.api.driver.FileSystemDriver;
import com.github.fge.fs.api.filestore.AbstractFileStore;
import com.github.fge.fs.api.fs.AbstractFileSystem;
import com.github.fge.fs.api.provider.AbstractFileSystemProvider;
import com.github.fge.fs.api.provider.FileSystemFactory;
import com.github.fge.fs.dropbox.DropboxFileSystemConstants;
import com.github.fge.fs.dropbox.attr.DropboxBasicFileAttributeViewLoader;
import com.github.fge.fs.dropbox.driver.DropboxFileSystemDriver;
import com.github.fge.fs.dropbox.filestore.DropboxFileStore;
import com.github.fge.fs.dropbox.fs.DropboxFileSystem;
import com.github.fge.fs.dropbox.path.DropboxPathContext;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public final class DropboxFileSystemFactory
    implements FileSystemFactory
{
    @Override
    public String getScheme()
    {
        return DropboxFileSystemConstants.URI_SCHEME;
    }

    @Override
    public AbstractFileSystem createFileSystem(
        final AbstractFileSystemProvider provider, final URI uri,
        final Map<String, ?> env)
        throws IOException
    {
        final DbxFiles files = createDbxFiles(env);
        final FileAttributeViewLoader<BasicFileAttributeView> loader
            = new DropboxBasicFileAttributeViewLoader(files);
        final AttributeViewProvider<BasicFileAttributeView> viewProvider
            = new BasicAttributeViewProvider(loader);
        final AttributesProvider<BasicFileAttributes> attributesProvider
            = new BasicFileAttributesProvider(loader);
        final FileAttributeViewFactory viewFactory
            = DefaultFileAttributeViewFactory.builder()
            .addViewProvider(viewProvider)
            .addAttributesProvider(BasicFileAttributes.class,
                attributesProvider)
            .build();
        final AbstractFileStore fileStore = new DropboxFileStore(viewFactory);
        final FileSystemDriver driver = new DropboxFileSystemDriver(files);
        return new DropboxFileSystem(provider,
            fileStore, new DropboxPathContext(uri), driver);
    }

    private static DbxFiles createDbxFiles(final Map<String, ?> env)
        throws IOException
    {
        final String accessToken;
        final String appName;

        final String dbxProperties
            = (String) env.get(DropboxFileSystemConstants.DROPBOX_PROPERTIES);

        if (dbxProperties == null) {
            accessToken = (String) env.get(
                DropboxFileSystemConstants.API_ACCESSTOKEN_KEY);
            appName = (String) env.get(
                DropboxFileSystemConstants.API_APPANE_KEY);
        } else {
            final Path path = Paths.get(dbxProperties);
            final Properties properties = new Properties();
            try (
                final Reader reader = Files.newBufferedReader(path);
            ) {
                properties.load(reader);
                accessToken = properties.getProperty(
                    DropboxFileSystemConstants.API_ACCESSTOKEN_KEY);
                appName = properties.getProperty(
                    DropboxFileSystemConstants.API_APPANE_KEY);
            }
        }

        Objects.requireNonNull(accessToken, "access token must not be null");
        Objects.requireNonNull(appName, "application name must not be null");

        final DbxRequestConfig config = new DbxRequestConfig(appName,
            Locale.US.toString());
        final DbxClientV2 client = new DbxClientV2(config, accessToken);
        return client.files;
    }
}
