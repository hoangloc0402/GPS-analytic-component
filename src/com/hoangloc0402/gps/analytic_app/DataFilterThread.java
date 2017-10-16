package com.hoangloc0402.gps.analytic_app;

import org.json.JSONObject;

import java.util.Calendar;

public class DataFilterThread extends Thread {
    private long currentTime;
    private final int offlineTime = 20000;
    public DataFilterThread(){

    }
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
                currentTime = Calendar.getInstance().getTimeInMillis();
                synchronized (AnalyticApp.hashMap){
                    AnalyticApp.hashMap.entrySet().removeIf(
                            x -> currentTime - x.getValue().getLong("time") > offlineTime
                    );
                    //AnalyticApp.hashMap.forEach((x,y) -> System.out.println(x+" "+y));

                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
