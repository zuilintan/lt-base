package com.lt.library.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.widget.Button;
import android.widget.EditText;

import java.util.Map;

/**
 * @作者: LinTan
 * @日期: 2020/8/9 10:59
 * @版本: 1.0
 * @描述: //EdtLinkBtnUtil, 联动EditText与Button
 * 1.0: Initial Commit
 */

public class EdtLinkBtnUtil implements TextWatcher {
    private final ArrayMap<EditText, Integer> mEditTextMap;
    private Button mButton;

    private EdtLinkBtnUtil() {
        mEditTextMap = new ArrayMap<>();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean isEnable = false;
        for (Map.Entry<EditText, Integer> entry : mEditTextMap.entrySet()) {
            EditText edt = entry.getKey();
            Integer btnEnableLength = entry.getValue();
            if (edt.getText().length() < btnEnableLength) {
                isEnable = false;
                break;
            } else {
                isEnable = true;
            }
        }
        mButton.setEnabled(isEnable);
    }

    public void addEvent() {
        for (Map.Entry<EditText, Integer> entry : mEditTextMap.entrySet()) {
            EditText editText = entry.getKey();
            editText.addTextChangedListener(this);
        }
    }

    public void delEvent() {
        for (Map.Entry<EditText, Integer> entry : mEditTextMap.entrySet()) {
            EditText editText = entry.getKey();
            editText.removeTextChangedListener(this);
        }
    }

    public static class Builder {
        private EdtLinkBtnUtil mEdtLinkBtnUtil;

        public Builder() {
            mEdtLinkBtnUtil = new EdtLinkBtnUtil();
        }

        public Builder addEditText(EditText editText, int btnEnableLength) {
            mEdtLinkBtnUtil.mEditTextMap.put(editText, btnEnableLength);
            return this;
        }

        public Builder setButton(Button button) {
            mEdtLinkBtnUtil.mButton = button;
            mEdtLinkBtnUtil.mButton.setEnabled(false);
            return this;
        }

        public EdtLinkBtnUtil build() {
            return mEdtLinkBtnUtil;
        }
    }
}
