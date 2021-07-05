package com.lt.library.util;

import android.os.Parcelable;

import com.lt.library.util.context.ContextUtil;
import com.tencent.mmkv.MMKV;

public class MmkvUtil {
    private MMKV mMMKV;

    private MmkvUtil() {
        MMKV.initialize(ContextUtil.getAppContext());
        mMMKV = MMKV.defaultMMKV();
    }

    private static MmkvUtil getInstance() {
        return MmkvUtilHolder.INSTANCE;
    }

    public static boolean encode(String key, boolean value) {
        return getInstance().mMMKV.encode(key, value);
    }

    public static boolean encode(String key, int value) {
        return getInstance().mMMKV.encode(key, value);
    }

    public static boolean encode(String key, long value) {
        return getInstance().mMMKV.encode(key, value);
    }

    public static boolean encode(String key, float value) {
        return getInstance().mMMKV.encode(key, value);
    }

    public static boolean encode(String key, double value) {
        return getInstance().mMMKV.encode(key, value);
    }

    public static boolean encode(String key, String value) {
        return getInstance().mMMKV.encode(key, value);
    }

    public static boolean encode(String key, Parcelable value) {
        return getInstance().mMMKV.encode(key, value);
    }

    public static boolean decodeBool(String key) {
        return getInstance().mMMKV.decodeBool(key);
    }

    public static boolean decodeBool(String key, boolean defValue) {
        return getInstance().mMMKV.decodeBool(key, defValue);
    }

    public static int decodeInt(String key) {
        return getInstance().mMMKV.decodeInt(key);
    }

    public static int decodeInt(String key, int defValue) {
        return getInstance().mMMKV.decodeInt(key, defValue);
    }

    public static long decodeLong(String key) {
        return getInstance().mMMKV.decodeLong(key);
    }

    public static long decodeLong(String key, long defValue) {
        return getInstance().mMMKV.decodeLong(key, defValue);
    }

    public static float decodeFloat(String key) {
        return getInstance().mMMKV.decodeFloat(key);
    }

    public static float decodeFloat(String key, float defValue) {
        return getInstance().mMMKV.decodeFloat(key, defValue);
    }

    public static double decodeDouble(String key) {
        return getInstance().mMMKV.decodeDouble(key);
    }

    public static double decodeDouble(String key, double defValue) {
        return getInstance().mMMKV.decodeDouble(key, defValue);
    }

    public static String decodeString(String key) {
        return getInstance().mMMKV.decodeString(key);
    }

    public static String decodeString(String key, String defValue) {
        return getInstance().mMMKV.decodeString(key, defValue);
    }

    public static <T extends Parcelable> T decodeParcelable(String key, Class<T> tClass) {
        return getInstance().mMMKV.decodeParcelable(key, tClass);
    }

    public static <T extends Parcelable> T decodeParcelable(String key, Class<T> tClass, T defValue) {
        return getInstance().mMMKV.decodeParcelable(key, tClass, defValue);
    }

    private static class MmkvUtilHolder {
        private static final MmkvUtil INSTANCE = new MmkvUtil();
    }
}
