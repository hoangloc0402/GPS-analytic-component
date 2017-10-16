package com.hoangloc0402.gps.analytic_app;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class AnalyticApp {
	public static HashMap<String,JSONObject> hashMap = new HashMap<>();
	public static void main(String[] args) throws Exception{
		int port = 6666;
		Receiver receiver = new Receiver("[Receiver]");
		receiver.connect();

		DataFilterThread f = new DataFilterThread();
		f.start();

		System.out.println("Server in running at port: "+port);
		ServerSocket welcomeSocket = new ServerSocket(port);

		try {
			while(true) {
				Socket connectionSocket = welcomeSocket.accept();
				DataHandlerThread h = new DataHandlerThread(connectionSocket);
				h.start();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {

			if (receiver.isConnected()) {
				try {
					receiver.client.disconnect();
				} catch (MqttException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
