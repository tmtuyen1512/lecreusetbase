package com.delfi.xmobile.lib.lecreusetbase.view.interfaces;

/**
 * Created by USER on 05/13/2019.
 */
public interface IStateful {
    void lock();

    void focus();

    void success();

    void error(String message);
}
