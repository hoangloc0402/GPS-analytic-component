package com.hoangloc0402.gps.analytic_app;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class HandleDataThread extends Thread {
    Socket connectionSocket;
    public HandleDataThread(Socket connectionSocket){
        this.connectionSocket = connectionSocket;
    }
    @Override
    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            String clientSentence = inFromClient.readLine();

            String send = clientSentence.toUpperCase() + "\n";//TODO: Add code to handle data

            outToClient.writeBytes(send);
            connectionSocket.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
