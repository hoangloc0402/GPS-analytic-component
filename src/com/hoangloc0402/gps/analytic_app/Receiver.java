package com.hoangloc0402.gps.analytic_app;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Receiver implements MqttCallback, IMqttActionListener {
    private static final String MqttUserName = "tedfdjcr";
    private static final String MqttPassword = "yoH3kIKmjikr";
    private static final String MqttServerURL = "tcp://m13.cloudmqtt.com:19122";

    private static final String TOPIC = "AssignmentNetworking";
    private static final String ENCODING = "UTF-8";
    private static final int QUALITY_OF_SERVICE = 2;

    private String name;
    private String clientId;
    public MqttAsyncClient client;
    private MemoryPersistence memoryPersistence;
    private IMqttToken connectToken;
    private IMqttToken subscribeToken;

    public Receiver(String name) { this.name = name; }

    public String getName() { return name; }

    public void connect() {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(MqttUserName);
            options.setPassword(MqttPassword.toCharArray());

            memoryPersistence = new MemoryPersistence();
            clientId = MqttAsyncClient.generateClientId();
            client = new MqttAsyncClient(MqttServerURL, clientId, memoryPersistence);

            client.setCallback(this);
            connectToken = client.connect(options, null, this);
        }
        catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return (client != null) && (client.isConnected());
    }



    @Override
    public void connectionLost(Throwable cause) {
        cause.printStackTrace();
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        if (asyncActionToken.equals(connectToken)) {
            System.out.println( String.format("%s successfully connected",name));
            try {
                subscribeToken = client.subscribe(TOPIC, QUALITY_OF_SERVICE, null, this);
            }
            catch (MqttException e) {
                e.printStackTrace();
            }
        }
        else if (asyncActionToken.equals(subscribeToken))
        {
            System.out.println( String.format("%s subscribed to the %s topic", name, TOPIC));

        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        exception.printStackTrace();
    }


    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception
    {
        if (!topic.equals(TOPIC)) {
            return;
        }

        String messageText = new String(message.getPayload(), ENCODING);
        storeMessage(messageText);//add message to map
        System.out.println( String.format("%s received %s: %s", name, topic, messageText));
        String[] keyValue = messageText.split(":");
        if (keyValue.length != 3) {
            return;
        }
    }
    private void storeMessage(String message) {
        try {
            JSONObject joList = new JSONObject(message).getJSONObject("list");
            JSONArray a = joList.names();
            synchronized (AnalyticApp.hashMap) {
                for (int i = 0; i < joList.length(); i++) {
                    JSONObject jsonObject = joList.getJSONObject(a.getString(i));
                    AnalyticApp.hashMap.put(jsonObject.getString("id"),jsonObject);
                }
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Delivery for a message has been completed
        // and all acknowledgments have been received
    }
}