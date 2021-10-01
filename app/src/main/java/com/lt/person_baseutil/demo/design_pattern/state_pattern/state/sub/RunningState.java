package com.lt.person_baseutil.demo.design_pattern.state_pattern.state.sub;

import com.lt.library.util.LogUtil;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.AbsState;

public class RunningState extends AbsState {
    @Override
    public void open() {
        LogUtil.d("动作无效, 电梯已处于运行中状态时, 禁止开启轿厢门");
    }

    @Override
    public void close() {
        LogUtil.d("动作忽略, 电梯已处于运行中状态时, 说明轿厢门同时已处于关闭状态");
    }

    @Override
    public void run() {
        LogUtil.d("动作忽略, 电梯已处于运行中状态");
    }

    @Override
    public void stop() {
        LogUtil.d("动作无效, 电梯已处于运行中状态时, 禁止停止电梯");
    }
}
