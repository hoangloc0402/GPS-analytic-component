package com.hoangloc0402.gps.analytic_app;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class AnalyticApp {
	public static HashMap<String,JSONObject> hashMap = new HashMap<>();
	public static void main(String[] args) throws Exception{
		int port = 6666;
		Receiver receiver = new Receiver("[Receiver]");
		receiver.connect();
		long curT = Calendar.getInstance().getTimeInMillis();
		for (int i=0;i<30;i++){//testing
			String s;
			s=""+i;
			hashMap.put(s,
					new JSONObject().put("time",curT+i*1000).put("id",i).put("name","Bao "+i)
							.put("lat",10+i).put("long",10+i)
			);
		}
		FilterDataThread f = new FilterDataThread();
		f.start();

		System.out.println("Server in running at port: "+port);
		ServerSocket welcomeSocket = new ServerSocket(port);

		try {
			while(true) {
				Socket connectionSocket = welcomeSocket.accept();
				HandleDataThread h = new HandleDataThread(connectionSocket);
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
