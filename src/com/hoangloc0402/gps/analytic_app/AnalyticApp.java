package com.hoangloc0402.gps.analytic_app;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Dictionary;
import java.util.Map;

public class AnalyticApp {
	public static void main(String[] args) throws Exception{
		int port = 5555;
		Receiver receiver = new Receiver("[Receiver]");
		receiver.connect();

		System.out.println("Server in running at port: "+port);
		ServerSocket welcomeSocket = new ServerSocket(port);

		try {
			while(true) {
				Socket connectionSocket = welcomeSocket.accept();
				HandleDataThread h = new HandleDataThread(connectionSocket,receiver);
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
