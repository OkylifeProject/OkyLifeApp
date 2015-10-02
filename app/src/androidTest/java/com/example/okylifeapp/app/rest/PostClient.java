package com.example.okylifeapp.app.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * Created by mordreth on 10/2/15.
 */
public class PostClient extends AsyncTask<String, Integer, String> {

    private String url;
    private Context context;
    private Object object;
    private AsyncResponse delegate;
    private List<NameValuePair> params;

    public PostClient(String url, Object object, AsyncResponse delegate, List<NameValuePair> params) {
        this.url = url;
        this.object = object;
        this.delegate = delegate;
        this.params = params;
    }

    public PostClient(String url, AsyncResponse delegate, List<NameValuePair> params) {
        this.url = url;
        this.delegate = delegate;
        this.params = params;
    }

    public PostClient(String url, Context context, Object object) {
        this.url = url;
        this.context = context;
        this.object = object;
    }


    @Override
    protected String doInBackground(String... params) {

        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(this.url);
        String response = null;
        request.addHeader("Accept", "application/json");
        try {
            request.setEntity(new UrlEncodedFormEntity(this.params));
            response = EntityUtils.toString(client.execute(request).getEntity());
        } catch (Exception ex) {
            Log.e("ServicioRestPOST", "Error!", ex);
            Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        this.delegate.processFinish(response);
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}

