package com.project;

import java.util.ArrayList;

// Todo hacer que la clase sea de estas ue solo se inician una vez
public class AppData {

    private ArrayList<ArrayList<String>> clientConnections;
    Process process;

    public AppData() {
        clientConnections = new ArrayList<>();
        process = null;
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

    public ArrayList<ArrayList<String>> getClientConnections() {
        return clientConnections;
    }

}
