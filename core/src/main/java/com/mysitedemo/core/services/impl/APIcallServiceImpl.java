package com.mysitedemo.core.services.impl;


import com.drew.lang.StringUtil;
import org.apache.http.entity.ContentType;
import com.mysitedemo.core.bean.messageBean;
import com.mysitedemo.core.services.APIcallService;
import com.mysitedemo.core.services.HttpClientFactory;
import com.mysitedemo.core.services.JsonConverterService;
import com.mysitedemo.core.utils.ObjectResponseHandler;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;

import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Reference;

@Slf4j
@Component(service=APIcallService.class)
public class APIcallServiceImpl implements APIcallService {

    public static final  ObjectResponseHandler responsehandler = new ObjectResponseHandler();

    @Reference
    private HttpClientFactory httpClientFactory;

    @Reference
    private JsonConverterService jsonConverterService;

    @Override
    public String callApi(String Text, int maxTokens) {
        String response = StringUtils.EMPTY;
        try{
            response=httpClientFactory.getExecutor().execute(httpClientFactory.post().bodyString(generatePrompt(Text,maxTokens), ContentType.APPLICATION_JSON)).handleResponse(responsehandler);
        }
        catch(Exception e)
        {
            log.debug("Exception on API call "+e.getMessage());
        }
        log.debug("API Request Response {}", response);
        return response;
    }


    private String generatePrompt(String body,int maxTokens)
    {
        messageBean message = new messageBean();
        if(maxTokens!=0)
        {
            message.setMaxTokens(maxTokens);
        }
        message.setPrompt(body);
        return jsonConverterService.convertToJsonString(message);
    }
}
