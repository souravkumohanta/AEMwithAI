package com.mysitedemo.core.services.impl;


import com.drew.lang.StringUtil;
import com.mysitedemo.core.bean.messageBean;
import com.mysitedemo.core.services.APIcallService;
import com.mysitedemo.core.services.HttpClientFactory;
import com.mysitedemo.core.services.JsonConverterService;
import com.mysitedemo.core.utils.ObjectResponseHandler;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;

import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Reference;

import javax.mail.internet.ContentType;

@Slf4j
@Component(service=APIcallService.class)
public class APIcallServiceImpl implements APIcallService {

    public static final ObjectResponseHandler responsehandler = new ObjectResponseHandler();

    @Reference
    private HttpClientFactory httpClientFactory;

    @Reference
    private JsonConverterService jsonConverterService;

    @Override
    public String callApi(String Text, String maxTokens) {
        String response = StringUtils.EMPTY;
        try{
            response=httpClientFactory.getExecutor().execute(httpClientFactory.post().body(generatePrompt(Text,maxTokens), ContentType.APPLICATION_JSON)).handleResponse(HANDLER);
        }
    }

    @Override
    public String generatePrompt(String body,int maxTokens)
    {
        messageBean message = new messageBean();
        if(maxTokens!=0)
        {
            message.setMaxTokens(maxTokens);
        }
        message.setPrompt(body);
        return JsonConverterService.convertToJsonString(message);
    }
}
