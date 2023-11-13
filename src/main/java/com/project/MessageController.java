package com.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class MessageController extends WebSocketServer {

    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    AppData appData;

    public MessageController(int port, AppData appData) {
        super(new InetSocketAddress(port));
        this.appData = appData;
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

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String clientId = getConnectionId(conn);
        appData.addClient(clientId, "ª");
        System.out.println("A connection with client " + clientId + " has created!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        String clientId = getConnectionId(conn);
        System.out.println("Message received > " + message);

        // Si el cliente esta pidiendo la lista de clientes
        if (message.equals("list")) {
            message = appData.getClientConnectionsString();
            message.substring(0, message.length() - 1);
        }

        // Si el cliente esta mandando su plataforma
        if (message.substring(0).equals("0")) {

            for (ArrayList<String> clientString : appData.getClientConnections()) {
                if (clientString.get(0).equals(clientId)) {
                    clientString.set(1, message.substring(1, message.length() - 1));
                }
            }

            message = appData.getClientConnectionsString();
            message.substring(0, message.length() - 1);
        }

        Main.runComand(message, appData);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        String clientId = getConnectionId(conn);
        int index = 0;

        for (int i = 0; i < appData.getClientConnections().size(); i++) {
            if (clientId.equals(appData.getClientConnections().get(i).get(0))) {
                index = i;
                break;
            }
        }

        appData.getClientConnections().remove(index);

        conn.close();
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        // Quan hi ha un error
        System.out.println("Error: " + ex.getMessage());
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
                // String[] lista = line.split(" ");
                // Main.runComand(lista, appData);
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
