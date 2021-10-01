package com.lt.person_baseutil.demo.design_pattern.state_pattern.state.sub;

import com.lt.library.util.LogUtil;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.AbsState;

public class ClosingState extends AbsState {
    @Override
    public void open() {
        LogUtil.d("动作无效, 轿厢门已处于关闭中状态时, 禁止开启轿厢门");
    }

    @Override
    public void close() {
        LogUtil.d("动作忽略, 轿厢门已处于关闭中状态");
    }

    @Override
    public void run() {
        LogUtil.d("动作无效, 轿厢门已处于关闭中状态时, 禁止运行电梯");
    }

    @Override
    public void stop() {
        LogUtil.d("动作忽略, 轿厢门已处于关闭中状态时, 说明电梯同时已处于停止状态");
    }
}
