package com.socurites.example.esper.listener;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import so.tree.http.*;

public class MyListener implements UpdateListener {

    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
        EventBean event = newEvents[0];
        System.out.println("[Catch!] house id : " + event.get("houseId"));
        String houseId = event.get("houseId").toString();
        
        HttpRequestThread httpRequestThread = new HttpRequestThread("GET", "http://localhost:8000/api/popularHouses/" + houseId);
        httpRequestThread.start();
        
        
    }
}

