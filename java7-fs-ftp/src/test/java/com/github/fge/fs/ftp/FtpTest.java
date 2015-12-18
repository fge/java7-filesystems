package com.github.fge.fs.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.Arrays;

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

        Arrays.stream(client.listFiles("x"))
            .map(FTPFile::getName)
            .forEach(System.out::println);

        System.out.println(client.logout());
    }
}
