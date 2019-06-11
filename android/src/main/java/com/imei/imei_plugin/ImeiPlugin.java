package com.imei.imei_plugin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * ImeiPlugin
 */
public class ImeiPlugin implements MethodCallHandler, PluginRegistry.RequestPermissionsResultListener {
    private Activity activity;
    private Result mResult;
    private MethodCall mCall;
    private static int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 192;

    private ImeiPlugin(Activity activity) {
        this.activity = activity;
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "imei_plugin");
        ImeiPlugin imeiPlugin = new ImeiPlugin(registrar.activity());
        channel.setMethodCallHandler(imeiPlugin);
        registrar.addRequestPermissionsResultListener(imeiPlugin);
    }

    private static void getImei(Context context, MethodCall call, Result result) {
        try {
            Integer index = call.argument("index");

            if (ContextCompat.checkSelfPermission((context), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                result.success(PhoneUtil.getImei(context, index != null ? index : 0));
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_PHONE_STATE))
                    result.error("IMEI_PLUGIN", "Permission Denied", new Exception("Permission Denied"));
                else
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        } catch (Exception ex) {
            result.error("IMEI_PLUGIN", ex.getMessage(), ex);
        }
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        mCall = call;
        mResult = result;
        if (call.method.equals("getImei")) {
            getImei(activity, mCall, mResult);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] strings, int[] ints) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (ints.length > 0 && ints[0] == PackageManager.PERMISSION_GRANTED) {
                getImei(activity, mCall, mResult);
            } else {
                // permission denied
                mResult.error("IMEI_PLUGIN", "Permission Denied", new Exception("Permission Denied"));
            }
        }
        return true;
    }
}
