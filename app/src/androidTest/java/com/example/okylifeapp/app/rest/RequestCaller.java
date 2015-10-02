package com.example.okylifeapp.app.rest;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mordreth on 10/2/15.
 */
public class RequestCaller {
    String hostUrl = "https://okylifeapi.herokuapp.com";
    HttpParams parameters;

    public void getById(String classUriPath, long id, AsyncResponse responseHandler) {
        String url = this.hostUrl + "/" + classUriPath + "/show";
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("id", String.valueOf(id)));
        url += "?" + URLEncodedUtils.format(parameters, "ISO-8859-1");
        GetClient getClient = new GetClient(url, responseHandler);
        getClient.execute();
    }

    public void postData(String classUriPath, AsyncResponse responseHandler, List<NameValuePair> params) {
        String url = this.hostUrl + "/" + classUriPath;
        PostClient postClient = new PostClient(url, responseHandler, params);
        postClient.execute();

    }
}
