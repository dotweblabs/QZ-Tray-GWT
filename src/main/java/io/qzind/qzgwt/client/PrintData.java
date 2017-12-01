package io.qzind.qzgwt.client;

import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class PrintData extends JSONObject {
    public void setCertificate(String certificate) {
        if(certificate == null) {
            put("certificate", JSONNull.getInstance());
        } else {
            put("certificate", new JSONString(certificate));
        }
    }
    public void setCall(String call) {
        put("call", new JSONString(call));
    }
    public void setPromise(JSONObject promise) {
        put("promise", promise);
    }
    public void setTimeStamp(Long timestamp) {
        put("timestamp", new JSONNumber(timestamp));
    }
    public void setUUID(String uuid) {
        put("uid", new JSONString(uuid));
    }
}
