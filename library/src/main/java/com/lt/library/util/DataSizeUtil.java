package com.lt.library.util;

import java.text.DecimalFormat;

public class DataSizeUtil {
    private DataSizeUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static String format(long byteSize) {
        StringBuilder result = new StringBuilder();
        double B = 1;
        double KB = 1024 * B;
        double MB = 1024 * KB;
        double GB = 1024 * MB;
        double TB = 1024 * GB;
        double PB = 1024 * TB;
        DecimalFormat df = new DecimalFormat("#.00");
        if (byteSize >= PB) {
            result.append(df.format(byteSize / PB)).append("PB");
        } else if (byteSize >= TB) {
            result.append(df.format(byteSize / TB)).append("TB");
        } else if (byteSize >= GB) {
            result.append(df.format(byteSize / GB)).append("GB");
        } else if (byteSize >= MB) {
            result.append(df.format(byteSize / MB)).append("MB");
        } else if (byteSize >= KB) {
            result.append(df.format(byteSize / KB)).append("KB");
        } else if (byteSize >= B) {
            result.append(byteSize / B).append("B");
        } else {
            result.append("0").append("B");
        }
        return result.toString();
    }
}
