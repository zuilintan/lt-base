package com.lt.person_baseutil.model.repo;

import com.lt.person_baseutil.R;
import com.lt.person_baseutil.model.entity.TestBean;

import java.util.ArrayList;
import java.util.List;

public class DataRepo {

    public static DataRepo getInstance() {
        return DataRepoHolder.INSTANCE;
    }

    public List<TestBean> getTestData() {
        int index = 0;
        List<TestBean> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(TestBean.create()
                             .setIco(String.valueOf(R.drawable.ic_android))
                             .setTitle("列表 - " + index++)
                             .setItemType(TestBean.ITEM_TYPE_CONTENT)
                             .setItemValue("State")
                             .build());
            list.add(TestBean.create()
                             .setIco(String.valueOf(R.drawable.ic_fire))
                             .setTitle("列表 - " + index++)
                             .setItemType(TestBean.ITEM_TYPE_SWITCH)
                             .setItemValue("false")
                             .build());
            list.add(TestBean.create()
                             .setIco(String.valueOf(R.drawable.ic_fire))
                             .setTitle("列表 - " + index++)
                             .setItemType(TestBean.ITEM_TYPE_SLIDE)
                             .setItemValue("2")
                             .build());
        }
        return list;
    }

    private static class DataRepoHolder {
        private static final DataRepo INSTANCE = new DataRepo();
    }
}
