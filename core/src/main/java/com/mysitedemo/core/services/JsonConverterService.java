package com.mysitedemo.core.services;

import com.fasterxml.jackson.databind.util.JSONPObject;

public interface JsonConverterService {

    /**
     * Convert Json Object to given object
     *
     * @param jsonpObject
     * @param clazz       type of class
     * @return @{@link Object}
     */
    @SuppressWarnings("rawtypes")
    Object convertToObject(JSONPObject jsonpObject, Class clazz);

    /**
     * Convert Json Object to given object
     *
     * @param jsonString
     * @param clazz      type of class
     * @return @{@link Object}
     */
    @SuppressWarnings("rawtypes")
    Object convertToObject(String jsonString, Class clazz);

    /**
     * Convert Json Object to given object
     *
     * @param object
     * @return @{@link String}
     */
    String convertToJsonString(Object object);

    /**
     * Convert Json Object to given object
     *
     * @param object
     * @return @{@link JSONPObject}
     */
    JSONPObject convertToJSONPObject(Object object);
}
