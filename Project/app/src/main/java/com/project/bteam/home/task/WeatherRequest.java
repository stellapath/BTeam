package com.project.bteam.home.task;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class WeatherRequest extends StringRequest {

    private String zone;

    public WeatherRequest(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener, String zone) {
        super(method, url, listener, errorListener);
        this.zone = zone;
    }
}
