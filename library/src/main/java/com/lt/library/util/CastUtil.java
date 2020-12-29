package com.lt.library.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auth: LinTan
 * @Date: 2020/12/29 23:43
 * @Desc: 类型转换工具类
 */

public class CastUtil {

    private CastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static <T> T obj2Obj(Object obj, Class<T> cls) {
        T result = null;
        if (cls.isInstance(obj)) {
            result = cls.cast(obj);
        }
        return result;
    }

    public static <T> ArrayList<T> obj2ArrayList(Object obj, Class<T> cls) {
        ArrayList<T> result = null;
        if (obj instanceof ArrayList) {
            result = new ArrayList<>();
            for (Object o : (List<?>) obj) {
                result.add(cls.cast(o));
            }
        }
        return result;
    }

    public static <K, V> HashMap<K, V> obj2HashMap(Object obj, Class<K> clsKey, Class<V> clsValue) {
        HashMap<K, V> result = null;
        if (obj instanceof HashMap<?, ?>) {
            result = new HashMap<>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                K k = clsKey.cast(entry.getKey());
                V v = clsValue.cast(entry.getValue());
                result.put(k, v);
            }
        }
        return result;
    }
}
