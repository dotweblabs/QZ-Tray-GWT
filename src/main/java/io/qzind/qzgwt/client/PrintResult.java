package io.qzind.qzgwt.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class PrintResult extends JSONObject {
    public String getUUID() {
        return get("uid").isString().stringValue();
    }
    public String getResult() {
        return get("result").isString().stringValue();
    }
    public void setUUID(String uuid) {
        put("uid", new JSONString(uuid));
    }
    public void setResult(String result) {
        put("result", new JSONString(result));
    }
}
