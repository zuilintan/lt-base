package com.lt.person_baseutil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lt.library.util.LogUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtil.v("msgV");
        LogUtil.v("tagV", "msgV");
        LogUtil.d("msgD");
        LogUtil.d("tagD", "msgD");
        LogUtil.i("msgI");
        LogUtil.i("tagI", "msgI");
        LogUtil.w("msgW");
        LogUtil.w("tagW", "msgW");
        LogUtil.e("msgE");
        LogUtil.e("tagE", "msgE");
        LogUtil.wtf("msgWtf");
        LogUtil.wtf("tagWtf", "msgWtf");
    }
}
