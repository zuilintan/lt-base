package com.lt.library.util.json.gson;

import static com.lt.library.util.json.gson.TypeFactory.$list;
import static com.lt.library.util.json.gson.TypeFactory.$map;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @作者: LinTan
 * @日期: 2018/12/25 17:16
 * @版本: 1.0
 * @描述: GsonUtil, 解决泛型擦除, 以及JSON转Map时, Integer变为Double的问题
 * 源址: https://www.jianshu.com/p/9ddd2d5b2d37
 * 1.0: Initial Commit
 * <p>
 * implementation 'com.google.code.gson:gson:2.8.5'
 */

public class GsonUtil {
    private final Gson mGson;

    private GsonUtil() {
        mGson = new Gson();
    }

    public static GsonUtil getInstance() {
        return GsonUtilHolder.INSTANCE;
    }

    public static <T> String objectToJson(T obj) {
        return getInstance().mGson.toJson(obj);
    }

    public static <T> T jsonToObject(String json, Class<T> cls) {
        return getInstance().mGson.fromJson(json, cls);
    }

    public static List<Object> jsonToList(String json) {
        return jsonToList(json, Object.class);
    }

    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Type type = $list(cls);
        return getInstance().mGson.fromJson(json, type);
    }

    public static Map<String, Object> jsonToMap(String json) {
        return jsonToMap(json, Object.class);
    }

    public static <T> Map<String, T> jsonToMap(String json, Class<T> cls) {
        Type type = $map(String.class, cls);
        return getInstance().mGson.fromJson(json, type);
    }

    public static List<List<Object>> jsonToListList(String json) {
        return jsonToListList(json, Object.class);
    }

    public static <T> List<List<T>> jsonToListList(String json, Class<T> cls) {
        Type type = $list($list(cls));
        return getInstance().mGson.fromJson(json, type);
    }

    public static List<Map<String, Object>> jsonToListMap(String json) {
        return jsonToListMap(json, Object.class);
    }

    public static <T> List<Map<String, T>> jsonToListMap(String json, Class<T> cls) {
        Type type = $list($map(String.class, cls));
        return getInstance().mGson.fromJson(json, type);
    }

    public static Map<String, List<Object>> jsonToMapList(String json) {
        return jsonToMapList(json, Object.class);
    }

    public static <T> Map<String, List<T>> jsonToMapList(String json, Class<T> cls) {
        Type type = $map(String.class, $list(cls));
        return getInstance().mGson.fromJson(json, type);
    }

    public static Map<String, Map<String, Object>> jsonToMapMap(String json) {
        return jsonToMapMap(json, Object.class);
    }

    public static <T> Map<String, Map<String, T>> jsonToMapMap(String json, Class<T> cls) {
        Type type = $map(String.class, $map(String.class, cls));
        return getInstance().mGson.fromJson(json, type);
    }

    private static class GsonUtilHolder {
        private static final GsonUtil INSTANCE = new GsonUtil();
    }
}
