package com.hoangloc0402.gps.analytic_app;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

public class Receiver implements MqttCallback, IMqttActionListener {
    public static final String MqttUserName = "zvzzpcnw";
    public static final String MqttPassword = "NAcTd5BI5Sfu";
    public static final String MqttServerURL = "tcp://m20.cloudmqtt.com:11297";

    public static final String TOPIC = "AssignmentNetworking";
    public static final String ENCODING = "UTF-8";
    public static final int QUALITY_OF_SERVICE = 2;

    protected LinkedList<String> ReceiverQueue = new LinkedList<>();
    protected String name;
    protected String clientId;
    protected MqttAsyncClient client;
    protected MemoryPersistence memoryPersistence;
    protected IMqttToken connectToken;
    protected IMqttToken subscribeToken;

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

    public void addMessage(String msg){
        ReceiverQueue.addLast(msg);
    }

    public String getMessage(){
        if (ReceiverQueue.isEmpty()) return "";
        else return ReceiverQueue.poll();
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
        ReceiverQueue.add(messageText);
        System.out.println( String.format("%s received %s: %s", name, topic, messageText));
        String[] keyValue = messageText.split(":");
        if (keyValue.length != 3) {
            return;
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Delivery for a message has been completed
        // and all acknowledgments have been received
    }
}