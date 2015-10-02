package com.example.okylifeapp.app.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by mordreth on 10/2/15.
 */
public class GetClient extends AsyncTask<Void, Void, String> {
    private String url;
    private Context context;
    private AsyncResponse delegate;

    public GetClient(String url, AsyncResponse delegate) {
        this.url = url;
        this.delegate = delegate;
    }

    public GetClient(String url, Context context) {
        this.url = url;
        this.context = context;
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


    @Override
    protected String doInBackground(Void... params) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(this.url);
        String response = null;
        request.addHeader("Accept", "application/json");
        try {
            response = EntityUtils.toString(client.execute(request).getEntity());
        } catch (Exception ex) {
            Log.e("ServicioRestGET", "Error!", ex);
            Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        this.delegate.processFinish(response);
    }
}