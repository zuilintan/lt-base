package com.lt.person_baseutil.demo.design_pattern.state_pattern;

public interface IHandle {
    /**
     * 开启轿厢门
     */
    void open();

    /**
     * 关闭轿厢门
     */
    void close();

    /**
     * 运行电梯
     */
    void run();

    /**
     * 停止电梯
     */
    void stop();
}
