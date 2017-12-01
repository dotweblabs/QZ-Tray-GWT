package io.qzind.qzgwt.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Window;
import io.qzind.qzgwt.client.callback.Callback;
import io.qzind.qzgwt.client.callback.OpenCallback;

/**
 *
 * Unit tests of {@link QZ}
 */
public class QzTrayTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "io.qzind.qzgwt.QzTray";
    }

    public void testConnect(){
        delayTestFinish(10000);
        QZ.connect(new OpenCallback() {
            @Override
            public void onOpen() {
                finishTest();
            }
        });
    }

    public void testGetVersion(){
        delayTestFinish(10000);
        QZ.connect(new OpenCallback() {
            @Override
            public void onOpen() {
                QZ.getVersion(new Callback<String>() {
                    @Override
                    public void onMessage(String result) {
                        Window.alert(result);
                        finishTest();
                    }
                });
            }
        });
    }

    public void testCertificate(){
        delayTestFinish(10000);
        QZ.connect(new OpenCallback() {
            @Override
            public void onOpen() {
                Window.alert("CALL Certificate");
                QZ.certificate(new Callback<String>() {
                    @Override
                    public void onMessage(String result) {
                        Window.alert(result);
                        finishTest();
                    }
                });
            }
        });
    }

}
