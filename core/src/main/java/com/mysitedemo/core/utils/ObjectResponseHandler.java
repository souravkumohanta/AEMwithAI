package com.mysitedemo.core.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

public class ObjectResponseHandler implements ResponseHandler<String> {
    // Create a BasicResponseHandler object to handle the HTTP response
    private BasicResponseHandler handler =new BasicResponseHandler();
    // Use the BasicResponseHandler object to extract the response string from the HTTP response
    @Override
    public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException
    {
        String responseString =handler.handleResponse(httpResponse);
        // Close the HTTP response to release resources
        HttpClientUtils.closeQuietly(httpResponse);
        return responseString;
    }

}
