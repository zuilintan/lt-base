package com.lt.library.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.widget.Button;
import android.widget.EditText;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @作者: LinTan
 * @日期: 2020/8/9 10:59
 * @版本: 1.0
 * @描述: EdtLinkBtnUtil, 联动EditText与Button
 * 1.0: Initial Commit
 */

public class EdtLinkBtnUtil implements TextWatcher {
    private static final int ENABLE_MAX_LENGTH_INFINITE = -1;
    private ArrayMap<EditText, int[]> mEditTextMap;
    private Button mButton;
    private Float mButtonDisableAlpha;
    private Integer[] mBtnEnableSpecialLengths;
    private Boolean mDiffSbcCase;

    private EdtLinkBtnUtil() {
    }

    private EdtLinkBtnUtil(Builder builder) {
        mEditTextMap = builder.mEditTextMap;
        mButton = builder.mButton;
        mButtonDisableAlpha = builder.mButtonDisableAlpha;
        mBtnEnableSpecialLengths = builder.mBtnEnableSpecialLengths;
        mDiffSbcCase = builder.mDiffSbcCase;
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
            mButton.setAlpha(mButtonDisableAlpha);
            mButton.setEnabled(false);
        }
    }

    private boolean checkLength() {
        boolean isEnable = true;
        for (Map.Entry<EditText, int[]> entry : mEditTextMap.entrySet()) {
            EditText edt = entry.getKey();
            int[] btnEnableRange = entry.getValue();
            Editable text = edt.getText();
            int length;
            if (mDiffSbcCase) {
                length = 0;
                String string = text.toString();
                for (int i = 0; i < string.length(); i++) {
                    char c = string.charAt(i);
                    Pattern p = Pattern.compile("[^\\x00-\\xff]");
                    Matcher m = p.matcher(String.valueOf(c));
                    if (!m.matches()) {
                        length++;
                    } else {
                        length += 2;
                    }
                }
            } else {
                length = text.length();
            }
            int btnEnableMinLength = btnEnableRange[0];
            int btnEnableMaxLength = btnEnableRange[1];
            boolean isGeBtnEnableMinLength;
            boolean isLeBtnEnableMaxLength;
            isGeBtnEnableMinLength = length >= btnEnableMinLength;
            if (btnEnableMaxLength == ENABLE_MAX_LENGTH_INFINITE) {
                isLeBtnEnableMaxLength = true;
            } else {
                isLeBtnEnableMaxLength = length <= btnEnableMaxLength;
            }
            if (mBtnEnableSpecialLengths == null || mBtnEnableSpecialLengths.length == 0 || !isSpecialLength(mBtnEnableSpecialLengths, length)) {
                if (!isGeBtnEnableMinLength || !isLeBtnEnableMaxLength) {
                    isEnable = false;
                    break;
                }
            }
        }
        return isEnable;
    }

    private boolean isSpecialLength(Integer[] specialLengths, int curLength) {
        boolean result = false;
        for (Integer length : specialLengths) {
            if (length != null && length == curLength) {
                result = true;
                break;
            }
        }
        return result;
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
        private ArrayMap<EditText, int[]> mEditTextMap;
        private Button mButton;
        private Float mButtonDisableAlpha;
        private Integer[] mBtnEnableSpecialLengths;
        private Boolean mDiffSbcCase = false;

        public Builder addEditText(EditText editText, int btnEnableLength) {
            return addEditText(editText, btnEnableLength, ENABLE_MAX_LENGTH_INFINITE);
        }

        public Builder addEditText(EditText editText, int btnEnableMinLength, int btnEnableMaxLength) {
            if (btnEnableMinLength < btnEnableMaxLength || btnEnableMaxLength == ENABLE_MAX_LENGTH_INFINITE) {
                if (mEditTextMap == null) {
                    mEditTextMap = new ArrayMap<>();
                }
                mEditTextMap.put(editText, new int[]{btnEnableMinLength, btnEnableMaxLength});
            } else {
                throw new IllegalArgumentException("btnEnableMinLength has to be less than btnEnableMaxLength, [" + btnEnableMinLength + ", " + btnEnableMaxLength + "]");
            }
            return this;
        }

        public Builder setBtnEnableSpecialLength(Integer... btnEnableLength) {
            mBtnEnableSpecialLengths = btnEnableLength;
            return this;
        }

        public Builder diffSbc() {
            mDiffSbcCase = true;
            return this;
        }

        public Builder setButton(Button button) {
            return setButton(button, 1);
        }

        public Builder setButton(Button button, float btnDisableAlpha) {
            mButton = button;
            mButtonDisableAlpha = btnDisableAlpha;
            return this;
        }

        public EdtLinkBtnUtil build() {
            if (mEditTextMap == null || mEditTextMap.isEmpty()) {
                throw new UnsupportedOperationException("addEditText() is not called");
            }
            if (mButton == null || mButtonDisableAlpha == null) {
                throw new UnsupportedOperationException("setButton() is not called");
            }
            EdtLinkBtnUtil edtLinkBtnUtil = new EdtLinkBtnUtil(this);
            edtLinkBtnUtil.changeBtnState();
            return edtLinkBtnUtil;
        }
    }
}
