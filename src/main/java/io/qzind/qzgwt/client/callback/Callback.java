package io.qzind.qzgwt.client.callback;

import com.google.gwt.core.client.GWT;

public interface Callback<T> {
    void onMessage(T result);
}