package com.project;

import java.util.ArrayList;

// Todo hacer que la clase sea de estas ue solo se inician una vez
public class AppData {

    private ArrayList<ArrayList<String>> clientConnections;
    private Process process;
    private String serverIp;

    public AppData(String serverIp) {
        clientConnections = new ArrayList<>();
        process = null;
        this.serverIp = serverIp;
    }

    public void addClient(String id, String platform) {
        ArrayList<String> comb = new ArrayList<>();
        comb.add(id);
        comb.add(platform);
        clientConnections.add(comb);
    }

    public void setProcess(Process p) {
        process = p;
    }

    public Process getProcess() {
        return process;
    }

    public String getServerIp() {
        return serverIp;
    }

    public ArrayList<ArrayList<String>> getClientConnections() {
        return clientConnections;
    }

    public String getClientConnectionsString() {
        String list = "";

        for (ArrayList<String> client : clientConnections) {
            list += " Client: " + client.get(0) + " from " + client.get(1) + " ";
        }

        return list;
    }

}
