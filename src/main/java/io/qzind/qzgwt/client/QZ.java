
package io.qzind.qzgwt.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import io.qzind.qzgwt.client.callback.Callback;
import com.sksamuel.gwt.websockets.Websocket;
import com.sksamuel.gwt.websockets.WebsocketListener;
import com.google.gwt.user.client.Timer;
import io.qzind.qzgwt.client.callback.OpenCallback;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class QZ {

    static boolean isOpen = false;
    static Websocket socket;
    static OpenCallback wsConnectCallback = null;
    static final Map<String,Callback<String>> callbackMap = new LinkedHashMap<>();;
    static private Timer t;
    static {
        socket = new Websocket("ws://localhost:8182/");
        socket.addListener(new WebsocketListener() {
            @Override
            public void onClose() {
                log("SOCKET CLOSE");
                isOpen = false;
                wsConnectCallback.onClose();
            }
            @Override
            public void onMessage(String s) {
                log("SOCKET MESSAGE " + s);
                processMessage(s);
            }
            @Override
            public void onOpen() {
                log("SOCKET OPEN");
                isOpen = true;
                wsConnectCallback.onOpen();
            }
        });
        t = new Timer() {
            @Override
            public void run() {
                socket.send("ping");
            }
        };
    }

    public static void connect(final OpenCallback callback) {
        wsConnectCallback = callback;
        socket.open();
        t.schedule(1000 * 60);
    }
    public static void find(String printer, Callback<String> callback) {
        // TODO
    }
    public static void findDefaultPrinter(final Callback<String> callback) {
        if(!isOpen) {
            socket.open();
            socket.addListener(new WebsocketListener() {
                @Override
                public void onClose() {
                    isOpen = false;
                }
                @Override
                public void onMessage(String s) {
                }
                @Override
                public void onOpen() {
                    doFindDefaultPrinter(callback);
                }
            });
        } else {
            doFindDefaultPrinter(callback);
        }

    }
    public static void print(Config config, PrintData data, Callback<String> callback) {

    }

    public static void getVersion(final Callback<String> callback) {
        if(!isOpen) {
            socket.open();
            socket.addListener(new WebsocketListener() {
                @Override
                public void onClose() {
                    isOpen = false;
                }
                @Override
                public void onMessage(String s) {
                }
                @Override
                public void onOpen() {
                    doGetVersion(callback);
                }
            });
        } else {
            doGetVersion(callback);
        }
    }

    public static void certificate(final Callback<String> callback) {
        if(!isOpen) {
            socket.open();
            socket.addListener(new WebsocketListener() {
                @Override
                public void onClose() {
                    isOpen = false;
                }
                @Override
                public void onMessage(String s) {
                }
                @Override
                public void onOpen() {
                    doCertificate(callback);
                }
            });
        } else {
            doCertificate(callback);
        }
    }

    public static void send(final String message, final Callback<String> callback) {
        if (!isOpen) {
            socket.open();
            socket.addListener(new WebsocketListener() {
                @Override
                public void onClose() {
                    isOpen = false;
                }

                @Override
                public void onMessage(String s) {
                    callback.onMessage(s);
                }

                @Override
                public void onOpen() {
                    doMessage(message, callback);
                }
            });
        } else {
            doMessage(message, callback);
        }
    }

    private static String generateUUID() {
        return UUID.uuid().replaceAll("-", "");
    }

    private static void processMessage(String s) {
        JSONObject jsonObject = JSONParser.parseStrict(s).isObject();
        if(jsonObject != null) {
            log("PROCESSING");
            String strResult = jsonObject.get("result") != null ? jsonObject.get("result").isString().stringValue() : null;
            String strUid = jsonObject.get("uid") != null ? jsonObject.get("uid").isString().stringValue() : null;
            log("UID       " + strUid);
            log("RESULT    " + strResult);
            log("MAP SIZE  " + callbackMap.size());
            if(strUid != null) {
                Callback<String> callback = callbackMap.get(strUid);
                callback.onMessage(strResult);
            }
        }
    }

    private static void doFindDefaultPrinter(Callback<String> callback) {
        String uid = generateUUID();
        callbackMap.put(uid, callback);

        PrintData printData = new PrintData();
        printData.setCall("printers.getDefault");
        printData.setPromise(new JSONObject());
        printData.setTimeStamp(new Date().getTime());
        printData.setUUID(uid);
        socket.send(printData.toString());
    }

    private static void doGetVersion(Callback<String> callback) {
        final String uid = generateUUID();
        callbackMap.put(uid, callback);

        PrintData printData = new PrintData();
        printData.setCall("getVersion");
        printData.setPromise(new JSONObject());
        printData.setTimeStamp(new Date().getTime());
        printData.setUUID(uid);
        socket.send(printData.toString());
    }

    private static void doCertificate(Callback<String> callback){
        final String uid = generateUUID();
        callbackMap.put(uid, callback);

        PrintData printData = new PrintData();
        printData.setCertificate(null);
        printData.setPromise(new JSONObject());
        printData.setTimeStamp(new Date().getTime());
        printData.setUUID(uid);
        socket.send(printData.toString());
    }

    private static void doMessage(String message, Callback<String> callback) {
        final String uid = generateUUID();
        JSONObject jsonObject = JSONParser.parseStrict(message).isObject();
        jsonObject.put("uid", new JSONString(uid));
        callbackMap.put(uid, callback);
        socket.send(jsonObject.toString());
    }

    public static native void log(String msg) /*-{
        $wnd.console.log(msg);
    }-*/;
}
