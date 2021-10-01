package com.lt.person_baseutil.demo.design_pattern.state_pattern.state.sub;

import com.lt.library.util.LogUtil;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.context.sub.Context;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.AbsState;

import java.util.concurrent.TimeUnit;

public class OpenedState extends AbsState {
    @Override
    public void open() {
        LogUtil.d("动作忽略, 轿厢门已处于已开启状态");
    }

    @Override
    public void close() {
        getContext().setState(Context.CLOSING_STATE);
        LogUtil.d("轿厢门关闭 start");
        new Thread(() -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));//模拟耗时动作
                getContext().setState(Context.CLOSED_STATE);
                LogUtil.d("轿厢门关闭 end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void run() {
        LogUtil.d("动作无效, 轿厢门已处于已开启状态时, 禁止运行电梯");
    }

    @Override
    public void stop() {
        LogUtil.d("动作忽略, 轿厢门已处于已开启状态时, 说明电梯同时已处于已停止状态");
    }
}
