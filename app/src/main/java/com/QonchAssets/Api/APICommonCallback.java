package com.QonchAssets.Api;

public class APICommonCallback {
    public interface Listener {
        public void onSuccess(Object body);
        public void onFailure(String reason);
    }

    private Listener listener;
}
