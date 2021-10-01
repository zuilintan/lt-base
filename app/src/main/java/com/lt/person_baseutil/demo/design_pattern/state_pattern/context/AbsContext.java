package com.lt.person_baseutil.demo.design_pattern.state_pattern.context;

import com.lt.person_baseutil.demo.design_pattern.state_pattern.IHandle;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.state.AbsState;

public abstract class AbsContext implements IHandle {
    private AbsState mState;

    public AbsState getState() {
        return mState;
    }

    public void setState(AbsState state) {
        mState = state;
        getState().setContext(this);
    }
}
