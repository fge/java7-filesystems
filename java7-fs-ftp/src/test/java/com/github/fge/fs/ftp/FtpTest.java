package com.github.fge.fs.ftp;

import com.github.fge.fs.ftp.driver.FtpInputStream;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FtpTest
{
    public static void main(final String... args)
        throws IOException
    {
        final String ip = "192.168.122.249";
        final String user = "sonar";
        final String passwd = "sonar";

        final FTPClient client = new FTPClient();
        client.setListHiddenFiles(true);
        client.connect(ip);
        if (!client.login(user, passwd))
            throw new IllegalArgumentException();

        final InputStream in = client.retrieveFileStream("config.xml");

        if (in == null) {
            System.err.println("Oops");
            System.err.println(client.getReplyCode());
            System.exit(1);
        }

        final Path path = Paths.get("/tmp/prout");

        try (
            final InputStream ftpin = new FtpInputStream(client, in);
        ) {
            Files.copy(ftpin, path);
        }

        System.out.println(client.logout());
    }
}
