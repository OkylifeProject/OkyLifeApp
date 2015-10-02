package com.example.okylifeapp.app.rest;

import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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

        StringBuffer bufferCadena = new StringBuffer("");
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(this.url);
            request.setHeader("Accept", "application/json");
            HttpResponse response = client.execute(request);
            BufferedReader stream = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String separator = "";
            String NL = System.getProperty("line.separator");
            while ((separator = stream.readLine()) != null) {
                bufferCadena.append(separator + NL);
            }
            stream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bufferCadena.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        this.delegate.processFinish(s);
    }
}