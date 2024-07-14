package com.example.aopstudy.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * @Author: auuuu4
 * @Date: 2024/07/09/14:23
 * @Description:
 */
public class JacksonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJsonString(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    public static <T> T toJavaObject(String json,Class<T> clazz) throws IOException {
        return (json == null) ? null : objectMapper.readValue(json,clazz);
    }



    public static <T> T toJavaObject(String json, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(json,typeReference);
    }

}
