package com.mysitedemo.core.services.impl;

import com.drew.lang.StringUtil;
import com.mysitedemo.core.services.HttpClientFactory;
import com.mysitedemo.core.services.config.HttpClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.fluent.Executor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Designate(ocd= HttpClientFactory.class)
@Component(service=HttpClientFactory.class)
public class HttpClientFactoryImpl implements HttpClientFactory {

    private Executor executor;
    private HttpClientConfig config;
    private String baseUrl;
    private CloseableHttpClient closeableHttpClient;

    @Reference
    private HttpClientFactory httpClientFactory;

    @Modified
    @Activate
    protected void activate(HttpClientConfig config) throws NoSuchFieldError, NoHttpResponseException, KeyStoreException, KeyManagementException, NoSuchAlgorithmException
    {
        log.info("CHATGPT Connection started for ",config.maxTimeOut());
        log.info("api key is ",config.apiKey());
        log.info("Host Name is ",config.apiHostName());
        log.info("API URI Type is ",config.apiUriPath());

        closeHttpConnection();
        this.config=config;
        if(this.config.apiHostName()==null)
        {
            log.error("Both Hostname is important ",config.apiHostName());
            throw new IllegalArgumentException("Both Hostnames are mandatory ");
        }
        this.baseUrl= StringUtils.join(this.config.apiHostName(),config.apiUriPath());
        initExecutor();

    }

    private void initExecutor() {
    }

    private void closeHttpConnection() {
    }
}