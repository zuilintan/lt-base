package com.lt.library.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

import com.lt.library.util.context.ContextUtil;

public class EdtHintUtil {

    private EdtHintUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static CharSequence formatHint(String hintText, int hintSize) {
        Spannable spannable = Spannable.Factory.getInstance().newSpannable(hintText);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(hintSize, true);
        spannable.setSpan(absoluteSizeSpan, 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return SpannableStringBuilder.valueOf(spannable);
    }

    public static CharSequence formatHint(int hintTextId, int hintSizeId) {
        Context context = ContextUtil.getAppContext();
        return formatHint(context.getString(hintTextId), (int) context.getResources().getDimension(hintSizeId));
    }
}
