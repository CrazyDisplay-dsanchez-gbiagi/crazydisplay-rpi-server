package com.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

public class MessageController extends WebSocketServer {

    ArrayList<User> users = new ArrayList<>(){{{
        add(new User("admin", "admin"));
        add(new User("super", "super"));
        add(new User("david", "david"));
    }}};
    

    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    AppData appData;

    public MessageController(int port, AppData appData) {
        super(new InetSocketAddress(port));
        this.appData = appData;
    }

    @Override
    public void onStart() {
        // Quan el servidor s'inicia
        int port = getAddress().getPort();
        System.out.println("WebSockets server running at: ws://" + appData.getServerIp() + ":" + port);
        System.out.println("Type 'exit' to stop and exit server.");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String clientId = getConnectionId(conn);
        //appData.addClient(clientId, "ª");
        System.out.println("A connection with client " + clientId + " has been created!");
        JSONObject objWlc = new JSONObject("{}");
        objWlc.put("type", "message");
        objWlc.put("from", "server");
        objWlc.put("value", "Welcome to the display server");
        conn.send(objWlc.toString()); 
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
    //     String clientId = getConnectionId(conn);
    //     System.out.println("Message received > " + message);

    //     // Si el cliente esta pidiendo la lista de clientes
    //     if (message.equals("list")) {
    //         message = appData.getClientConnectionsString();
    //     }

    //     // Si el cliente esta mandando su plataforma
    //     if (message.substring(0, 1).equals("~")) {
    //         for (ArrayList<String> clientString : appData.getClientConnections()) {
    //             if (clientString.get(0).equals(clientId)) {
    //                 clientString.set(1, message.substring(1, message.length()));
    //             }
    //         }

    //         message = appData.getClientConnectionsString();
    //     }
    //     System.out.println(message);
    //     Main.runComand(message, appData);
    // }
        String clientId = getConnectionId(conn);
        String promptPantalla;
        try {
            JSONObject objRequest = new JSONObject(message);
            String type = objRequest.getString("type");

            // Si el cliente manda un mensaje
            if (type.equalsIgnoreCase("message")) {
                if (objRequest.getString("value").equals("list")) {
                    promptPantalla = appData.getClientConnectionsString();
                }
                else {
                    promptPantalla = objRequest.getString("value");
                }
                Main.runComand(promptPantalla, appData);
                // Imprimir mensaje en pantalla
                System.out.println("Client '" + clientId + "'': " + objRequest.getString("value"));
            }
            // Si el cliente manda el login
            else if (type.equalsIgnoreCase("login")) {
                JSONObject objResponse = new JSONObject("{}");
                boolean userValid = false;
                for (User user : users) {
                    if (user.getUser().equals(objRequest.getString("user")) && user.getPassword().equals(objRequest.getString("password"))) {
                        userValid = true;
                    }
                }
                if (userValid) {
                    objResponse.put("type", "login");
                    objResponse.put("valid", true);
                    conn.send(objResponse.toString());
                } else {
                    objResponse.put("type", "login");
                    objResponse.put("valid", false);
                    conn.send(objResponse.toString());
                }
            }
            else if (type.equalsIgnoreCase("platform")) {
                appData.addClient(clientId, objRequest.getString("name"));
                promptPantalla = appData.getClientConnectionsString();
                Main.runComand(promptPantalla, appData);
                System.out.println("Client " + clientId + " from: " + objRequest.getString("name"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
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

        if (appData.getClientConnections().size() == 0) {
            Main.runComand(appData.getServerIp(), appData);
        } else {
            Main.runComand(appData.getClientConnectionsString(), appData);
        }

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
                    Main.killComand(appData.getProcess());
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
