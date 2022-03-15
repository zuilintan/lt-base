package com.lt.library.util.json.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @作者: LinTan
 * @日期: 2020/12/27 14:01
 * @版本: 1.0
 * @描述: 自定义TypeAdapter, 修复默认TypeAdapter{@link ObjectTypeAdapter}, 在Json转Map时, 类型Integer变为Double的问题
 * <p>
 * implementation 'com.google.code.gson:gson:2.8.5'
 */

public class NumberTypeAdapter extends TypeAdapter<Object> {

    @Override
    public void write(JsonWriter out, Object value) throws IOException {
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        switch (token) {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(read(in));
                }
                in.endArray();
                return list;

            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap<>();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), read(in));
                }
                in.endObject();
                return map;

            case STRING:
                return in.nextString();

            case NUMBER://change point
                Object numberResult;
                double doubleNumber = in.nextDouble();
                long longNumber = (long) doubleNumber;
                if (doubleNumber == longNumber) {
                    if (longNumber > Integer.MAX_VALUE) {
                        numberResult = longNumber;
                    } else {
                        numberResult = (int) longNumber;
                    }
                } else {
                    if (doubleNumber > Float.MAX_VALUE) {
                        numberResult = doubleNumber;
                    } else {
                        numberResult = (float) doubleNumber;
                    }
                }
                return numberResult;

            case BOOLEAN:
                return in.nextBoolean();

            case NULL:
                in.nextNull();
                return null;

            default:
                throw new IllegalStateException();
        }
    }
}
