package com.hoangloc0402.gps.analytic_app;

import org.json.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HandleDataThread extends Thread {
    private Socket connectionSocket;
    public HandleDataThread(Socket connectionSocket){
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
            JSONObject jo = new JSONObject(message);
            String request = jo.getString("request");
            System.out.println(request);
            if (request.equals("friend_location")) {
                JSONObject sendJO = new JSONObject();
                synchronized (AnalyticApp.hashMap) {
                    sendJO.put("list", AnalyticApp.hashMap);
                }
                return sendJO.toString();
            } else return "Error";
        }
        catch (Exception e){
            //System.out.println("WRONG MESSAGE: "+message);
            return "Error";
        }
    }

}
