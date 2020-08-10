package com.delfi.xmobile.lib.lecreusetbase.view.interfaces;

import android.content.Context;

/**
 * Created by DANHPC on 30/01/2018.
 */

public interface IView {
    void showProgressDialog(boolean show);
    void showToastMessage(int idMessage);

    Context getContext();
}
