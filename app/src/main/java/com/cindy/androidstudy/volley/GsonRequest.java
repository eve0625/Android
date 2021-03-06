package com.cindy.androidstudy.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonRequest<T> extends JsonRequest<T> {

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;

    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, clazz, headers, "", listener, errorListener);
    }

    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers, String requestBody, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.POST, url, clazz, headers, requestBody, listener, errorListener);
    }

    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers, Object bodyObject, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.POST, url, clazz, headers, new Gson().toJson(bodyObject), listener, errorListener);
    }

    private GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, String requestBody, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        this.clazz = clazz;
        this.headers = headers;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

}
