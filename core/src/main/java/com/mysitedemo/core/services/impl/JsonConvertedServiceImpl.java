package com.mysitedemo.core.services.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mysitedemo.core.services.JsonConverterService;
import org.osgi.service.component.annotations.Component;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Component(service = JsonConverterService.class)
public class JsonConvertedServiceImpl implements JsonConverterService {

    @SuppressWarnings("unchecked")
    @Override
    public Object convertToObject(JSONPObject jsonpObject, @SuppressWarnings("rawtypes") Class clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonpObject.toString(), clazz);
        } catch (IOException e) {
            log.debug("IOException while converting JSON to {} class {}", clazz.getName(), e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object convertToObject(String jsonString, @SuppressWarnings("rawtypes") Class clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            log.debug("IOException while converting JSON to {} class {}", clazz.getName(), e.getMessage());
        }
        return null;
    }

    @Override
    public String convertToJsonString(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY).writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            log.debug("IOException while converting object {} to Json String {}", object.getClass().getName(),
                    e.getMessage());
        }
        return null;
    }

    @Override
    public JSONPObject convertToJSONPObject(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writeValueAsString(object);
            return mapper.readValue(jsonString, JSONPObject.class);
        } catch (IOException e) {
            log.debug("IOException while converting object {} to Json String {}", object.getClass().getName(),
                    e.getMessage());
        }
        return null;
    }


}
