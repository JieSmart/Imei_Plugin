package com.imei.imei_plugin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

public class PhoneUtil {

    /**
     * 获取IMEI
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getImei(Context context,int index) {
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (manager == null) {
                return "";
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return manager.getImei(index);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return manager.getDeviceId(index);
            } else {
                Method getImei = manager.getClass().getMethod("getImei", int.class);
                return (String) getImei.invoke(manager, index);
            }
        } catch (Throwable throwable) {
            return "";
        }
    }
}

