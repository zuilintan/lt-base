package com.lt.library.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @作者: LinTan
 * @日期: 2018/12/25 17:16
 * @版本: 1.0
 * @描述: //GsonUtil, 注意引入依赖
 * 源址: https://www.jianshu.com/p/5ad3d87a7e47
 * 1.0: Initial Commit
 * <p>
 * implementation 'com.google.code.gson:gson:2.8.5'
 */

public class GsonUtil {
    private static Gson sGson = new Gson();

    private GsonUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static String objectToJson(Object obj) {
        String json = null;
        if (sGson != null) {
            json = sGson.toJson(obj);
        }
        return json;
    }//object对象转换为json字符串

    public static <T> T jsonToObject(String json, Class<T> cls) {
        T object = null;
        if (sGson != null) {
            object = sGson.fromJson(json, cls);
        }
        return object;
    }//json字符串转换为泛型Object(JavaBean)

    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        List<T> list = null;
        if (sGson != null) {
            list = new ArrayList<>();
            //Plan A 存在编译期间泛型类型被擦除的问题, 不推荐
            //list = sGson.fromJson(json, new TypeToken<List<T>>() {}.getType());
            //Plan B 需要Gson 2.2.4以上的版本
            //list = sGson.fromJson(json, com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, cls));
            //Plan C 网上多数的解决方案, 推荐
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement elem : array) {
                list.add(sGson.fromJson(elem, cls));
            }
        }
        return list;
    }//json字符串转换为List

    public static <T> Map<String, T> jsonToMap(String json) {
        Map<String, T> map = null;
        if (sGson != null) {
            map = sGson.fromJson(json, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }//json字符串转换为Map

    public static <T> List<Map<String, T>> jsonToMapList(String json) {
        List<Map<String, T>> mapList = null;
        if (sGson != null) {
            mapList = sGson.fromJson(json, new TypeToken<List<Map<String, T>>>() {
            }.getType());
        }
        return mapList;
    }//json字符串转换为List, 并且list中每个元素都是map

    public static <T> List<T> objectCastList(Object obj, Class<T> cls) {
        List<T> list = null;
        if (obj instanceof List<?>) {
            list = new ArrayList<>();
            for (Object o : (List<?>) obj) {
                list.add(cls.cast(o));
            }
        }
        return list;
    }//Object强转为List，避免使用@SuppressWarnings("unchecked"); eg: Map<String, Object>强转为Map<String, List<T>>，可与jsonToMapList搭配使用
}
