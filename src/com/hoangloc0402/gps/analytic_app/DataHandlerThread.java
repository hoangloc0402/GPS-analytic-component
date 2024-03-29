package com.hoangloc0402.gps.analytic_app;

import org.json.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHandlerThread extends Thread {
    private Socket connectionSocket;
    public DataHandlerThread(Socket connectionSocket){
        this.connectionSocket = connectionSocket;
    }
    @Override
    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            String clientMessage = inFromClient.readLine();

            String send = handle(clientMessage) + "\n";//TODO: Add code to handle data

            outToClient.writeBytes(send);
            connectionSocket.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private String handle(String message){
        try {
            System.out.println("REQUEST:"+message);
            JSONObject jo = new JSONObject(message);
            String request = jo.getString("request");
            String id = jo.getString("id");
            if (request.equals("friend_location")) {
                JSONObject sendJO = new JSONObject();
                synchronized (AnalyticApp.hashMap) {
                    HashMap<String,JSONObject> h = new HashMap<>(AnalyticApp.hashMap);
                    h.entrySet().removeIf(x -> x.getKey().equals(id));
                    //System.out.println(h);
                    sendJO.put("list", h);
                }
                return sendJO.toString();
            } else return "Error";
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return "Error";
        }
    }

}
