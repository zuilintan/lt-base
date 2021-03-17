package com.lt.library.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.widget.Button;
import android.widget.EditText;

import java.util.Map;
import java.util.Objects;

/**
 * @作者: LinTan
 * @日期: 2020/8/9 10:59
 * @版本: 1.0
 * @描述: //EdtLinkBtnUtil, 联动EditText与Button
 * 1.0: Initial Commit
 */

public class EdtLinkBtnUtil implements TextWatcher {
    private static final int ENABLE_MAX_LENGTH_INFINITE = -1;
    private final ArrayMap<EditText, int[]> mEditTextMap;
    private Button mButton;
    private Float mButtonDisableAlpha;

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
        changeBtnState();
    }

    private void changeBtnState() {
        if (checkLength()) {
            mButton.setAlpha(1);
            mButton.setEnabled(true);
        } else {
            mButton.setAlpha(Objects.requireNonNull(mButtonDisableAlpha, "setButton() is not called"));
            mButton.setEnabled(false);
        }
    }

    private boolean checkLength() {
        boolean isEnable = true;
        for (Map.Entry<EditText, int[]> entry : mEditTextMap.entrySet()) {
            EditText edt = entry.getKey();
            int[] textProperty = entry.getValue();
            int length = edt.getText().length();
            int btnEnableMinLength = textProperty[0];
            int btnEnableMaxLength = textProperty[1];
            boolean isGeBtnEnableMinLength;
            boolean isLeBtnEnableMaxLength;
            isGeBtnEnableMinLength = length >= btnEnableMinLength;
            if (btnEnableMaxLength == ENABLE_MAX_LENGTH_INFINITE) {
                isLeBtnEnableMaxLength = true;
            } else {
                isLeBtnEnableMaxLength = length <= btnEnableMaxLength;
            }
            if (!isGeBtnEnableMinLength || !isLeBtnEnableMaxLength) {
                isEnable = false;
                break;
            }
        }
        return isEnable;
    }

    public void addEvent() {
        for (Map.Entry<EditText, int[]> entry : mEditTextMap.entrySet()) {
            EditText editText = entry.getKey();
            editText.addTextChangedListener(this);
        }
    }

    public void delEvent() {
        for (Map.Entry<EditText, int[]> entry : mEditTextMap.entrySet()) {
            EditText editText = entry.getKey();
            editText.removeTextChangedListener(this);
        }
    }

    public static class Builder {
        private final EdtLinkBtnUtil mEdtLinkBtnUtil;

        public Builder() {
            mEdtLinkBtnUtil = new EdtLinkBtnUtil();
        }

        public Builder addEditText(EditText editText, int btnEnableLength) {
            return addEditText(editText, btnEnableLength, ENABLE_MAX_LENGTH_INFINITE);
        }

        public Builder addEditText(EditText editText, int btnEnableMinLength, int btnEnableMaxLength) {
            if (btnEnableMinLength < btnEnableMaxLength || btnEnableMaxLength == ENABLE_MAX_LENGTH_INFINITE) {
                mEdtLinkBtnUtil.mEditTextMap.put(editText, new int[]{btnEnableMinLength, btnEnableMaxLength});
            } else {
                throw new IllegalArgumentException("btnEnableMinLength has to be less than btnEnableMaxLength, [" + btnEnableMinLength + ", " + btnEnableMaxLength + "]");
            }
            return this;
        }

        public Builder setButton(Button button) {
            return setButton(button, 1);
        }

        public Builder setButton(Button button, float btnDisableAlpha) {
            mEdtLinkBtnUtil.mButton = button;
            mEdtLinkBtnUtil.mButtonDisableAlpha = btnDisableAlpha;
            return this;
        }

        public EdtLinkBtnUtil build() {
            if (mEdtLinkBtnUtil.mEditTextMap.isEmpty()) {
                throw new NullPointerException("addEditText() is not called");
            }
            if (Objects.isNull(mEdtLinkBtnUtil.mButton)) {
                throw new NullPointerException("setButton() is not called");
            }
            mEdtLinkBtnUtil.changeBtnState();
            return mEdtLinkBtnUtil;
        }
    }
}
