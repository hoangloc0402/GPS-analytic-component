package com.hoangloc0402.gps.analytic_app;

import org.json.JSONObject;

import java.util.Calendar;

public class FilterDataThread extends Thread {
    private long currentTime;
    private final int offlineTime = 20000;
    public FilterDataThread(){

    }
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
                currentTime = Calendar.getInstance().getTimeInMillis();
                System.out.println("CurreTime:"+currentTime);
                synchronized (AnalyticApp.hashMap){
                    AnalyticApp.hashMap.entrySet().removeIf(
                            x -> currentTime - x.getValue().getLong("time") > offlineTime
                    );
                    AnalyticApp.hashMap.forEach((x,y) -> System.out.println(x+" "+y));

                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}
