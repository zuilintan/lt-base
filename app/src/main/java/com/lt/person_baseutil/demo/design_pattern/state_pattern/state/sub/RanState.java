package com.lt.person_baseutil.demo.design_pattern.state_pattern.state.sub;

import com.lt.library.util.LogUtil;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.context.sub.Context;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.AbsState;

import java.util.concurrent.TimeUnit;

public class RanState extends AbsState {
    @Override
    public void open() {
        LogUtil.d("动作无效, 电梯已处于已运行状态时, 禁止开启轿厢门");
    }

    @Override
    public void close() {
        LogUtil.d("动作忽略, 电梯已处于已运行状态时, 说明轿厢门同时已处于关闭状态");
    }

    @Override
    public void run() {
        LogUtil.d("动作忽略, 电梯已处于已运行状态");
    }

    @Override
    public void stop() {
        getContext().setState(Context.STOPPING_STATE);
        LogUtil.d("电梯停止 start");
        new Thread(() -> {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(2));//模拟耗时动作
                getContext().setState(Context.STOPPED_STATE);
                LogUtil.d("电梯停止 end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
