package com.github.fge.fs.api.provider;

import com.github.fge.fs.api.driver.FileSystemDriver;
import com.github.fge.fs.api.driver.FileSystemIoDriver;
import com.github.fge.fs.api.driver.FileSystemOptionsChecker;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbstractFileSystemProviderTest
{
    private AbstractFileSystemProvider provider;

    private Path path;

    private FileSystemOptionsChecker checker;
    private FileSystemIoDriver ioDriver;
    private FileSystemDriver driver;

    @BeforeMethod
    public void initProvider()
    {
        provider = spy(new TestFileSystemProvider());
        path = mock(Path.class);
        checker = mock(FileSystemOptionsChecker.class);
        ioDriver = mock(FileSystemIoDriver.class);
        driver = mock(FileSystemDriver.class);
        when(driver.getOptionsChecker()).thenReturn(checker);
        when(driver.getIoDriver()).thenReturn(ioDriver);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void newInputStreamTest()
        throws IOException
    {
        final Set<OpenOption> opts = mock(Set.class);
        when(checker.checkReadOptions(any())).thenReturn(opts);

        doReturn(driver).when(provider).getDriver(path);

        provider.newInputStream(path, new OpenOption[1]);

        verify(ioDriver).getInputStream(same(path), same(opts));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void newOutputStreamTest()
        throws IOException
    {
        final Set<OpenOption> opts = mock(Set.class);
        when(checker.checkWriteOptions(any())).thenReturn(opts);

        doReturn(driver).when(provider).getDriver(path);

        provider.newOutputStream(path, new OpenOption[1]);

        verify(ioDriver).getOutputStream(same(path), same(opts));
    }
}
