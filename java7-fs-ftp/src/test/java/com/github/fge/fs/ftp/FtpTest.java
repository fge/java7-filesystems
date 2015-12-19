package com.github.fge.fs.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;

public final class FtpTest
{
    public static void main(final String... args)
        throws IOException
    {
        final String ip = "192.168.122.249";
        final String user = "sonar";
        final String passwd = "sonar";

        final FTPClient client = new FTPClient();
        client.connect(ip);
        if (!client.login(user, passwd))
            throw new IllegalArgumentException();

        final FTPFile file = client.listFiles("x")[0];
        System.out.println(file.isDirectory());
        System.out.println(file.hasPermission(FTPFile.READ_PERMISSION,
            FTPFile.USER_ACCESS));

        System.out.println(client.logout());
    }
}
