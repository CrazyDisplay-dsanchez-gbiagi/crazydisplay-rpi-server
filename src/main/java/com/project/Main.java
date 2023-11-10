package com.project;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        int port = 8888;
        String localIp = getLocalIPAddress();
        System.out.println("Local server IP: " + localIp);

        String cd[] = { "cd", "/home/ieti" };
        runComand(cd);

        String cmd[] = { "./dev/rpi-rgb-led-matrix/utils/text-scroller", "-f",
                "~/dev/bitmap-fonts/bitmap/cherry/cherry-10-b.bdf", "--led-cols=64", "--led-rows=64",
                "--led-slowdown-gpio=4", "--led-no-hardware-pulse", "'" + localIp + "'" };
        runComand(cmd);

        // Deshabilitar SSLv3 per clients Android
        java.lang.System.setProperty("jdk.tls.client.protocols", "TLSv1,TLSv1.1,TLSv1.2");

        MessageController server = new MessageController(port);
        server.runServerBucle();
    }

    public static String getLocalIPAddress() throws SocketException, UnknownHostException {
        String localIp = "";
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface ni = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress ia = inetAddresses.nextElement();
                if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia.isSiteLocalAddress()) {
                    System.out.println(ni.getDisplayName() + ": " + ia.getHostAddress());
                    localIp = ia.getHostAddress();
                    // Si hi ha múltiples direccions IP, es queda amb la última
                }
            }
        }

        // Si no troba cap direcció IP torna la loopback
        if (localIp.compareToIgnoreCase("") == 0) {
            localIp = InetAddress.getLocalHost().getHostAddress();
        }
        return localIp;
    }

    public static void runComand(String[] cmd) {
        System.out.println("Iniciant comanda...");
        try {
            // objecte global Runtime
            Runtime rt = java.lang.Runtime.getRuntime();

            // executar comanda en subprocess
            Process p = rt.exec(cmd);
            // donem un temps d'execució
            TimeUnit.SECONDS.sleep(1);
            // el matem si encara no ha acabat
            if (p.isAlive())
                p.destroy();
            p.waitFor();
            // comprovem el resultat de l'execució
            System.out.println("Comanda 1 exit code=" + p.exitValue());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // finish
        System.out.println("Comandes finalitzades.");
    }
}
