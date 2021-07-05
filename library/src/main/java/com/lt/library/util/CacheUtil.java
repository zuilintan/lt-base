package com.lt.library.util;

import android.content.Context;
import android.os.Environment;

import com.lt.library.util.context.ContextUtil;

import java.io.File;
import java.util.Objects;

public class CacheUtil {
    private CacheUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取缓存大小
     *
     * @return 缓存大小
     */
    public static String getCacheSize() {
        Context context = ContextUtil.getAppContext();
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            cacheSize += getFolderSize(Objects.requireNonNull(context.getExternalCacheDir()));
        }
        return SizeUtil.format(cacheSize);
    }

    /**
     * 清理缓存
     */
    public static void clearCache() {
        Context context = ContextUtil.getAppContext();
        delFolder(context.getCacheDir());
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            delFolder(Objects.requireNonNull(context.getExternalCacheDir()));
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param file {@link Context#getExternalCacheDir()}
     *             {@link Context#getExternalFilesDir(String)}
     * @return 文件夹大小
     */
    private static long getFolderSize(File file) {
        long size = 0;
        File[] fileList = file.listFiles();
        for (File subFile : fileList) {
            if (subFile.isDirectory()) {
                size = size + getFolderSize(subFile);
            } else {
                size = size + subFile.length();
            }
        }
        return size;
    }

    /**
     * 删除文件夹
     *
     * @param file {@link Context#getExternalCacheDir()}
     *             {@link Context#getExternalFilesDir(String)}
     * @return 是否删除成功
     */
    private static boolean delFolder(File file) {
        if (file.isDirectory()) {
            String[] fileList = file.list();
            for (String subFile : fileList) {
                boolean success = delFolder(new File(file, subFile));
                if (!success) {
                    return false;
                }
            }
        }
        return file.delete();
    }
}