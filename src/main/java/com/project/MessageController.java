package com.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class MessageController extends WebSocketServer {

    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private LinkedList<String> _last5Messages = new LinkedList<>();

    public MessageController(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String clientId = getConnectionId(conn);
        System.out.println("A connection with client " + clientId + " has created!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onClose'");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message received > " + message);
        System.out.println("Iniciant comanda...");

        String cmd[] = { "./dev/rpi-rgb-led-matrix/utils/text-scroller", "-f",
                "~/dev/bitmap-fonts/bitmap/cherry/cherry-10-b.bdf", "--led-cols=64", "--led-rows=64",
                "--led-slowdown-gpio=4", "--led-no-hardware-pulse", "'" + message + "'" };

        /*
         * ./dev/rpi-rgb-led-matrix/utils/text-scroller -f
         * ~/dev/bitmap-fonts/bitmap/cherry/cherry-10-b.bdf --led-cols=64 --led-rows=64
         * --led-slowdown-gpio=4 --led-no-hardware-pulse "message"
         */

        try {
            // objecte global Runtime
            Runtime rt = java.lang.Runtime.getRuntime();

            // executar comanda en subprocess
            Process p = rt.exec(cmd);
            // donem un temps d'execució
            TimeUnit.SECONDS.sleep(5);
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

    @Override
    public void onError(WebSocket conn, Exception ex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onError'");
    }

    @Override
    public void onStart() {
        // Quan el servidor s'inicia
        String host = getAddress().getAddress().getHostAddress();
        int port = getAddress().getPort();
        System.out.println("WebSockets server running at: ws://" + host + ":" + port);
        System.out.println("Type 'exit' to stop and exit server.");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    public void runServerBucle() {
        boolean running = true;
        try {
            System.out.println("Starting server");
            start();
            while (running) {
                String line;
                line = in.readLine();
                if (line.equals("exit")) {
                    running = false;
                }
            }
            System.out.println("Stopping server");
            stop(1000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getConnectionId(WebSocket connection) {
        String name = connection.toString();
        return name.replaceAll("org.java_websocket.WebSocketImpl@", "").substring(0, 3);
    }

}
