package com.example.sprinconnect.service.mappers;

import java.util.HashMap;
import java.util.Map;

public class ResponseMapper {

    public static Map<String, Object> responseMapper(Object data, String message,  boolean status, int statusCode) {

        Map<String, Object> responseMap = new HashMap<>();

        if(data != null) responseMap.put("data", data);
        responseMap.put("message", message);
        responseMap.put("status", status);
        responseMap.put("statusCode", statusCode);

        return responseMap;
    }
}
