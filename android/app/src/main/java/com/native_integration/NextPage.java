package com.native_integration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class NextPage extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_page);
        button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                callReactNative();
                MainApplication application = (MainApplication) NextPage.this.getApplication();
                ReactNativeHost reactNativeHost = application.getReactNativeHost();
                ReactInstanceManager reactInstanceManager = reactNativeHost.getReactInstanceManager();
                ReactContext reactContext = reactInstanceManager.getCurrentReactContext();

                reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("Success", "Receive Message from NextPage!");

//                if (reactContext != null) {
//                    CatalystInstance catalystInstance = reactContext.getCatalystInstance();
//                    WritableNativeArray params = new WritableNativeArray();
//                    params.pushString("Receive Message!");
//                    catalystInstance.callFunction("receiveModule", "receiveMethod", params);
//                }
            }
        });
    }

    @ReactMethod
    void callReactNative() {
//        Activity activity = getCurrentActivity();
//        if (activity != null) {
        MainApplication application = (MainApplication) this.getApplication();
        ReactNativeHost reactNativeHost = application.getReactNativeHost();
        ReactInstanceManager reactInstanceManager = reactNativeHost.getReactInstanceManager();
        ReactContext reactContext = reactInstanceManager.getCurrentReactContext();

        if (reactContext != null) {
            CatalystInstance catalystInstance = reactContext.getCatalystInstance();
            WritableNativeArray params = new WritableNativeArray();
            params.pushString("Receive Message!");
            catalystInstance.callFunction("receiveModule", "receiveMethod", params);
        }
//        }
    }
}
