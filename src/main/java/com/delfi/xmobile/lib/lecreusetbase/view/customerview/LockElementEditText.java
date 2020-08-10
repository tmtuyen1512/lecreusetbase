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
import android.util.AttributeSet;


public class LockElementEditText extends ElementEditText {

    public LockElementEditText(Context context) {
        super(context);
    }

    public LockElementEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockElementEditText(Context context,
                               AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        super.init();
        lock();
    }


    @Override
    public void lock() {
        super.lock();
        this.setEditable(false);
        /*this.setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.drawable_edittext_lock_on_init));*/
    }

    @Override
    public void focus() {
        super.focus();
        this.setEditable(true);
        this.setFocus();
        /*this.setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.drawable_edittext_unlock_on_focus));*/
    }

    @Override
    public void success() {
        super.success();
        this.setEditable(false);
        /*this.setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.drawable_edittext_lock_on_succes));*/
    }

    @Override
    public void error(String message) {
        super.error(message);
        this.setFocus();
        this.setError(message);
        /*this.setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.drawable_edittext_unlock_on_error));*/
    }

    @Override
    public StatusState getStatus() {
        return super.getStatus();
    }
}
