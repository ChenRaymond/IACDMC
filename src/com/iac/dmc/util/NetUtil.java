package com.iac.dmc.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class NetUtil {

    public static boolean isNetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
    }


    public static boolean isWIFIActivate(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                .isWifiEnabled();
    }

    public static void changeWIFIStatus(Context context, boolean status) {
        ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
        .setWifiEnabled(status);
    }
}
