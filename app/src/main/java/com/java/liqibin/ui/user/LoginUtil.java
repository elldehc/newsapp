package com.java.liqibin.ui.user;

import android.content.Context;

class LoginUtil {
    static void checkLogin(Context context, LoginCallBack callBack) {

    }


    @FunctionalInterface
    interface LoginCallBack {
        void callBak(boolean success);
    }
}
