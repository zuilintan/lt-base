package com.lt.person_baseutil.demo.design_pattern.state_pattern.state.sub;

import com.lt.library.util.LogUtil;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.context.sub.Context;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.AbsState;

import java.util.concurrent.TimeUnit;

public class StoppedState extends AbsState {
    @Override
    public void open() {
        getContext().setState(Context.OPENING_STATE);
        LogUtil.d("轿厢门开启 start");
        new Thread(() -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));//模拟耗时动作
                getContext().setState(Context.OPENED_STATE);
                LogUtil.d("轿厢门开启 end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void close() {
        LogUtil.d("动作忽略, 电梯已处于停止状态时, 说明轿厢门同时已处于关闭状态");
    }

    @Override
    public void run() {
        getContext().setState(Context.RUNNING_STATE);
        LogUtil.d("电梯运行 start");
        new Thread(() -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(2));//模拟耗时动作
                getContext().setState(Context.RAN_STATE);
                LogUtil.d("电梯运行 end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void stop() {
        LogUtil.d("动作忽略, 电梯已处于停止状态");
    }
}
