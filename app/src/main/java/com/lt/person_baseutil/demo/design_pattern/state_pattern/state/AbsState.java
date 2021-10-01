package com.lt.person_baseutil.demo.design_pattern.state_pattern.state;

import com.lt.person_baseutil.demo.design_pattern.state_pattern.IHandle;
import com.lt.person_baseutil.demo.design_pattern.state_pattern.context.AbsContext;

public abstract class AbsState implements IHandle {
    private AbsContext mContext;

    public AbsContext getContext() {
        return mContext;
    }

    public void setContext(AbsContext context) {
        mContext = context;
    }
}
