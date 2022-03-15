package com.lt.library.base.recyclerview;

import android.view.View;

/**
 * @Auth: LinTan
 * @Date: 2021/11/19 15:25
 * @Desc: RecyclerView Item的点击事件接口
 */

public interface OnItemClickListener {
    void onClick(View view, int position);
}
