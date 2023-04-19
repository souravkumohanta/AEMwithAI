package com.mysitedemo.core.services;

 public interface APIcallService {
    /**
     * authenticate user with provided details
     *
     * @param Text    bodytext to be summarized
     * @param maxTokens    maximum words to be summarized
     * @return @{@link String}
     */
     String callApi(String Text,String maxTokens);
}
