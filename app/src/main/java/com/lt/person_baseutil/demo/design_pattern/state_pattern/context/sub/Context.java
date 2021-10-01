package com.lt.person_baseutil.demo.design_pattern.state_pattern.context.sub;

import com.lt.person_baseutil.demo.design_pattern.state_pattern.context.AbsContext;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.AbsState;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.sub.ClosedState;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.sub.ClosingState;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.sub.OpenedState;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.sub.OpeningState;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.sub.RanState;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.sub.RunningState;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.sub.StoppedState;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.sub.StoppingState;

public class Context extends AbsContext {
    public static final AbsState OPENING_STATE = new OpeningState();
    public static final AbsState OPENED_STATE = new OpenedState();
    public static final AbsState CLOSING_STATE = new ClosingState();
    public static final AbsState CLOSED_STATE = new ClosedState();
    public static final AbsState RUNNING_STATE = new RunningState();
    public static final AbsState RAN_STATE = new RanState();
    public static final AbsState STOPPING_STATE = new StoppingState();
    public static final AbsState STOPPED_STATE = new StoppedState();

    {
        setState(CLOSED_STATE);
    }

    @Override
    public void open() {
        getState().open();
    }

    @Override
    public void close() {
        getState().close();
    }

    @Override
    public void run() {
        getState().run();
    }

    @Override
    public void stop() {
        getState().stop();
    }
}
