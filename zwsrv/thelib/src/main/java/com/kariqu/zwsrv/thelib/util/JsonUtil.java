package com.kariqu.zwsrv.thelib.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 10/03/17.
 */
public class JsonUtil {

    private static JsonUtil instance = null;

    public static final synchronized JsonUtil getInstance() {
        if (instance==null) {
            instance = new JsonUtil();
        }
        return instance;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonUtil() {
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,
                false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private static ObjectMapper objectMapper() {
        return JsonUtil.getInstance().objectMapper;
    }

    public static <T>List<T> convertJson2ModelList(String jsonString, Class<T> clazz){
        List<T> list = null;
        try {
            List<Map> l = objectMapper().readValue(jsonString, objectMapper().getTypeFactory().constructCollectionType(
                    List.class, HashMap.class));

            ObjectMapper mapper = new ObjectMapper();
            list = new ArrayList<>();
            for(Map map: l){
                list.add(mapper.convertValue(map, clazz));
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return list;
    }

    public static <T>T convertJson2Model(String jsonString, Class<T> clazz){
        if(jsonString == null || "".equals(jsonString)){
            return null;
        }
        try {
            return objectMapper().readValue(jsonString, clazz);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertObject2Json(Object o) {
        if(o != null){
            try {
                return objectMapper().writeValueAsString(o);
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return "";
    }

    public static <T>List<T> convertJson2RawTypeList(String jsonString, Class<T> clazz){
        if(jsonString == null || "".equals(jsonString)){
            return null;
        }
        List<T> list = null;
        try {
            list = objectMapper().readValue(jsonString, objectMapper().getTypeFactory().constructCollectionType(
                    List.class, clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
