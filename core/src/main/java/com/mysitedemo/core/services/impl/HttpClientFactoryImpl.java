package com.mysitedemo.core.services.impl;

import com.mysitedemo.core.services.HttpClientFactory;
import com.mysitedemo.core.services.config.HttpClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.apache.http.config.Registry;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Designate(ocd= HttpClientConfig.class)
@Component(service=HttpClientFactory.class)
public class HttpClientFactoryImpl implements HttpClientFactory {

    private Executor executor;
    private HttpClientConfig config;
    private String baseUrl;
    private CloseableHttpClient closeableHttpClient;

    @Reference
    private HttpClientBuilderFactory httpClientBuilderFactory;

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
    /*
    initExecutor() which initializes an HTTP client and an executor to execute HTTP requests.
    Here is a detailed explanation of what each declaration
     */
    /**
     * Initializes the HTTP executor.
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     */
    private void initExecutor() throws KeyManagementException,NoSuchAlgorithmException,KeyStoreException {
        PoolingHttpClientConnectionManager connectionManager=null;

        RequestConfig requestConfig= initRequestConfig();

        HttpClientBuilder builder= httpClientBuilderFactory.newBuilder();

        builder.setDefaultRequestConfig(requestConfig);

        if(config.SSLValue()==true)
        {
            connectionManager=initPoolingManagerWithRelaxedSSl();
        }
        else {
            connectionManager=new PoolingHttpClientConnectionManager();
        }

        // Close any expired connections in the connection pool
        connectionManager.closeExpiredConnections();

        // Set the maximum total number of connections allowed in the pool
        connectionManager.setMaxTotal(config.maxTotalOpenConnections());

        // Set the maximum number of connections allowed per route in the pool
        connectionManager.setDefaultMaxPerRoute(config.maxConcurrentConnectionPerRoute());

        // Set the connection pool manager for the HttpClient builder
        builder.setConnectionManager(connectionManager);

        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        headers.add(new BasicHeader("Authorization", "Bearer " + config.apiKey()));
        builder.setDefaultHeaders(headers);
        builder.setKeepAliveStrategy(keepAliveStrategy);

        closeableHttpClient=builder.build();
        executor=Executor.newInstance(closeableHttpClient);

    }

    private PoolingHttpClientConnectionManager initPoolingManagerWithRelaxedSSl() throws KeyManagementException,KeyStoreException,NoSuchAlgorithmException{

        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
        SSLContextBuilder sslContextBuilder=new SSLContextBuilder();

        sslContextBuilder.loadTrustMaterial(new TrustAllStrategy());
        SSLConnectionSocketFactory sslsf=new SSLConnectionSocketFactory(sslContextBuilder.build(),NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry= RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https",sslsf).build();
        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        return poolingHttpClientConnectionManager;
    }

    private RequestConfig initRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(Math.toIntExact(TimeUnit.SECONDS.toMillis(config.maxTimeOut())))
                .setSocketTimeout(Math.toIntExact(TimeUnit.SECONDS.toMillis(config.socketTimeout())))
                .setConnectionRequestTimeout(
                        Math.toIntExact(TimeUnit.SECONDS.toMillis(config.connectionRequestTimeout())))
                .build();
    }

    private void closeHttpConnection() {

        if(null!=closeableHttpClient)
        {
            try{
                closeableHttpClient.close();
            }
            catch(final IOException e){
                log.error("Error in connection closure ");
                log.debug("IOException while clossing API, {}",e.getMessage());
        }
        }
    }

    @Deactivate
    private void deactivate(){
        closeHttpConnection();
    }

    @Override
    public Executor getExecutor()
    {
        return executor;
    }

    @Override
    public Request post(){
       return Request.Post(baseUrl);
    }

    ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy() {

        @Override
        public long getKeepAliveDuration(HttpResponse response, HttpContext context) {

           /* The commented-out code within the method is an example of how the keep-alive duration could
            be determined based on the timeout parameter specified in the Keep-Alive header of the response.
            However, this code has been commented out and the default duration specified in the configuration
            file is used instead.*/

        /*
             * HeaderElementIterator headerElementIterator = new BasicHeaderElementIterator(
             * response.headerIterator(HTTP.CONN_KEEP_ALIVE));
             *
             * while (headerElementIterator.hasNext()) { HeaderElement headerElement =
             * headerElementIterator.nextElement(); String param = headerElement.getName();
             * String value = headerElement.getValue(); if (value != null &&
             * param.equalsIgnoreCase("timeout")) { return
             * TimeUnit.SECONDS.toMillis(Long.parseLong(value)); } }
             */

            return TimeUnit.SECONDS.toMillis(config.keepAliveConnections());
        }
    };
}