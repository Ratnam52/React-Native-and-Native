package com.native_integration;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeArray;

public class NativeStart extends ReactContextBaseJavaModule {

    NativeStart(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "NativeStart";
    }

    @ReactMethod
    void reactToNative() {
        ReactApplicationContext context = getReactApplicationContext();
        Intent intent = new Intent(context, NextPage.class);
        context.startActivity(intent);
    }

    @ReactMethod
    void callReactNative() {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            MainApplication application = (MainApplication) activity.getApplication();
            ReactNativeHost reactNativeHost = application.getReactNativeHost();
            ReactInstanceManager reactInstanceManager = reactNativeHost.getReactInstanceManager();
            ReactContext reactContext = reactInstanceManager.getCurrentReactContext();

            if (reactContext != null) {
                CatalystInstance catalystInstance = reactContext.getCatalystInstance();
                WritableNativeArray params = new WritableNativeArray();
                params.pushString("Receive Message from NativeStart!");
                catalystInstance.callFunction("receiveModule", "receiveMethod", params);
            }
        }
    }

    @ReactMethod
    void messageFromNative() {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            MainApplication application = (MainApplication) activity.getApplication();
            ReactNativeHost reactNativeHost = application.getReactNativeHost();
            ReactInstanceManager reactInstanceManager = reactNativeHost.getReactInstanceManager();
            ReactContext reactContext = reactInstanceManager.getCurrentReactContext();

            if (reactContext != null) {
                CatalystInstance catalystInstance = reactContext.getCatalystInstance();
                WritableNativeArray params = new WritableNativeArray();
                params.pushString("Receive Message!");
                catalystInstance.callFunction("receiveModule", "receiveMethod", params);
            }
        }
    }

    @ReactMethod
    void activityCallBack(@NonNull Callback callback) {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            callback.invoke(activity.getClass().getSimpleName());
        }
    }
}
