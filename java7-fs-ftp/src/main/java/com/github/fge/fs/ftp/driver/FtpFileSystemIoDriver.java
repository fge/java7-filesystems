package com.github.fge.fs.ftp.driver;

import com.github.fge.fs.api.directory.DefaultDirectoryStream;
import com.github.fge.fs.api.driver.ReadOnlyFileSystemIoDriver;
import com.github.fge.fs.ftp.directory.FtpDirectorySpliterator;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.DirectoryStream;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class FtpFileSystemIoDriver
    extends ReadOnlyFileSystemIoDriver
{
    private static final int[] FTP_ENTITIES = {
        FTPFile.USER_ACCESS,
        FTPFile.GROUP_ACCESS,
        FTPFile.WORLD_ACCESS
    };

    private static final Map<AccessMode, Integer> FTP_ACCESS_MAP;

    static {
        final Map<AccessMode, Integer> map = new EnumMap<>(AccessMode.class);

        map.put(AccessMode.READ, FTPFile.READ_PERMISSION);
        map.put(AccessMode.WRITE, FTPFile.WRITE_PERMISSION);
        map.put(AccessMode.EXECUTE, FTPFile.EXECUTE_PERMISSION);

        FTP_ACCESS_MAP = Collections.unmodifiableMap(map);
    }

    private final FTPClient client;

    public FtpFileSystemIoDriver(final FTPClient client)
    {
        this.client = client;
    }

    @Override
    public InputStream getInputStream(final Path path,
        final Set<OpenOption> options)
        throws IOException
    {
        final String ftpPath = getFtpPath(path);
        final FTPFile ftpFile = getFtpFileForPath(ftpPath, AccessMode.READ);
        if (!ftpFile.isFile())
            throw new IOException(ftpPath + " is not a regular file");
        final InputStream stream = client.retrieveFileStream(ftpPath);
        if (stream == null)
            throw new IOException("FTP error :" + client.getReplyCode());
        return new FtpInputStream(client, stream);
    }

    @Override
    public DirectoryStream<Path> getDirectoryStream(final Path dir,
        final DirectoryStream.Filter<? super Path> filter)
        throws IOException
    {
        final String ftpPath = getFtpPath(dir);
        final FTPFile ftpFile = getFtpFileForPath(ftpPath, AccessMode.READ);
        if (!ftpFile.isDirectory())
            throw new NotDirectoryException(ftpPath);

        final FTPListParseEngine engine = client.initiateListParsing(ftpPath);
        final Spliterator<Path> spliterator
            = new FtpDirectorySpliterator(dir, engine);
        final Stream<Path> stream = StreamSupport.stream(spliterator, true);
        return new DefaultDirectoryStream(stream, filter);
    }

    @Override
    public void checkAccess(final Path path, final AccessMode... modes)
        throws IOException
    {
        final String ftpPath = getFtpPath(path);
        getFtpFileForPath(ftpPath, modes);
    }

    private static String getFtpPath(final Path path)
    {
        return path.toAbsolutePath().toString().substring(1);
    }

    private FTPFile getFtpFileForPath(final String ftpPath,
        final AccessMode... modes)
        throws IOException
    {
        final FTPFile[] array = client.listFiles(ftpPath);
        if (array.length == 0)
            throw new NoSuchFileException(ftpPath);

        final FTPFile ftpFile = array[0];
        int ftpMode;

        for (final AccessMode mode: modes) {
            ftpMode = FTP_ACCESS_MAP.get(mode);
            for (final int entity: FTP_ENTITIES)
                if (ftpFile.hasPermission(ftpMode, entity))
                    return ftpFile;
        }

        throw new AccessDeniedException(ftpPath);
    }
}
