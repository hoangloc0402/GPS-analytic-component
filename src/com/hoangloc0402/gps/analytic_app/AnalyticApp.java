package com.hoangloc0402.gps.analytic_app;

import org.eclipse.paho.client.mqttv3.MqttException;

public class AnalyticApp {
	public static void main(String[] args) {
		Receiver receiver = new Receiver("[Receiver]");
		receiver.connect();
		int i=0;
		try {
			while (true) {
				try {
					Thread.sleep(2000);


				}
				catch (Exception e) {
					e.printStackTrace();
				}
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
