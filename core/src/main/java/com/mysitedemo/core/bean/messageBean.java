package com.mysitedemo.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class messageBean
{
    @JsonProperty("maxtokens")
    int maxTokens;

    @JsonProperty("prompt")
    String prompt;

    @JsonProperty("timeout")
    String timeout;

    @JsonProperty("temperature")
    double temperature = 0.5;

    @JsonProperty("top_p")
    int topP = 1;

    @JsonProperty("frequency_penalty")
    double frequencyPenalty = 0.5;

    @JsonProperty("presence_penalty")
    double presencePenalty = 0.5;

}
