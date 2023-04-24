package com.mysitedemo.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.w3c.dom.Attr;

@ObjectClassDefinition(name = "Http Client Config values ",description = "Adjust api keys and other config values using this")
public @interface HttpClientConfig {

    @AttributeDefinition(name="Api Host Name",description = "Enter api host name ex: https://api.openai.com",type = AttributeType.STRING)
    String apiHostName() default "https://api.openai.com";

    @AttributeDefinition(name = "API URI type path ",description = "API URI type path, e.g. /services/int/v2",type = AttributeType.STRING)
    String apiUriPath() default "/v1/engines/davinci/completions";

    @AttributeDefinition(name = "API Key to access open AI",description = "CHAT GPT Open AI key ",type = AttributeType.STRING)
    String apiKey() default "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";

    @AttributeDefinition(name = "Connection Time out ",description = "Duration before connection time outs, default is 30",type = AttributeType.INTEGER)
    int maxTimeOut() default 30;

    @AttributeDefinition(name = " Keep Connection Alive until in seconds ",description = "Default Keep alive connection in seconds, default value is 15",type = AttributeType.INTEGER)
    int keepAliveConnections() default 15;

    @AttributeDefinition(name = "Socket time out in seconds ",description = "Maximum time in seconds before socket timeout, default is 15",type = AttributeType.INTEGER)
    int socketTimeout() default 15;

    @AttributeDefinition(name = "Maximum number of concurrent connections per route", description = "Set the maximum number of concurrent connections per route, default 5", type = AttributeType.INTEGER)
    int maxConcurrentConnectionPerRoute() default 2;

    @AttributeDefinition(name = "Time before connection Timesout ",description = "Maximum time allowed before conn. timeout , default is 30",type = AttributeType.INTEGER)
    int connectionRequestTimeout() default 30;

    @AttributeDefinition(name = "Maximum number of Total open connections ",description = "Maximum number of total open connections allowed , default is 10",type = AttributeType.INTEGER)
    int maxTotalOpenConnections() default 10;

    @AttributeDefinition(name = "Realxed SSL ",description = "Defined if self certified certificated are allowed for SSL transport ",type = AttributeType.BOOLEAN)
    boolean SSLValue() default true;
}
