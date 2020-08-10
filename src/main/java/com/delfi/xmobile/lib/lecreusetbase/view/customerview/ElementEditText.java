package com.delfi.xmobile.lib.lecreusetbase.view.customerview;
/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

import com.delfi.xmobile.lib.lecreusetbase.view.interfaces.IStateful;
import com.delfi.xmobile.lib.xcore.common.SoundManager;


public class ElementEditText extends TextInputEditText implements IStateful {

    private StatusState status;

    public ElementEditText(Context context) {
        super(context);
        init();
    }

    public ElementEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ElementEditText(Context context,
                           AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {

    }

    @Override
    public void lock() {
        status = StatusState.INIT;
    }

    @Override
    public void focus() {
        this.setFocus();
        status = StatusState.FOCUS;
    }

    @Override
    public void success() {
        status = StatusState.SUCCESS;
    }

    @Override
    public void error(String message) {
        this.setFocus();
        this.setError(message);

        SoundManager.getInstance().PlayError(getContext());
        status = StatusState.ERROR;
    }

    protected void setEditable(boolean editable) {
        if (editable) {
            this.setEnabled(true);
            this.setFocusable(true);
            this.setFocusableInTouchMode(true);

        } else {
            this.setEnabled(false);
            this.setFocusable(false);
            this.setFocusableInTouchMode(false);
        }
    }

    protected void setFocus() {
        this.setSelectAllOnFocus(true);
        this.clearFocus();
        this.requestFocus();
        this.selectAll();
    }

    public StatusState getStatus() {
        return status;
    }
}
