package com.lt.library.util.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * @Auth: LinTan
 * @Date: 2020/12/30 0:27
 * @Desc: FastJson工具类
 */

public class FastJsonUtil {

    private FastJsonUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static <T> String objectToJson(T obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T jsonToObject(String json, Class<T> cls) {
        return JSON.parseObject(json, cls);
    }

    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        return JSON.parseArray(json, cls);
    }

    public static Map<String, Object> jsonToMap(String json) {
        return JSON.parseObject(json);
    }

    public static List<Map<String, Object>> jsonToListMap(String json) {
        return JSON.parseObject(json, new TypeReference<List<Map<String, Object>>>() {
        });
    }
}
