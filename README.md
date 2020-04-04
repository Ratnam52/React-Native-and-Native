# React-Native-and-Native

Hi, This project is nothing about, How can transfer the data between react native and native (Android & IOS) or else move to native.

Now we going to see two senarios
1. Create a new react native project and do data transfer
2. Integrate an existing native project with react native

# Senario 1:

Create an new react native project
cmd : react-native init native_integration



# App.js

import React from 'react';
import { View, Text, TouchableOpacity, NativeModules, DeviceEventEmitter } from 'react-native';
import BatchedBridge from "react-native/Libraries/BatchedBridge/BatchedBridge";

export default class App extends React.Component {

  toNative = () => {
    NativeModules.NativeStart.reactToNative();
  }

  toReact = () => {
    NativeModules.NativeStart.callReactNative();
  }

  receiveMethod = (message) => {
    alert(message);
  }

  componentDidMount() {
    this.mounted = true;
    this.receiveEvent = this.receiveEvent.bind(this);
    DeviceEventEmitter.addListener('Success', this.receiveEvent)
  }

  receiveEvent(message) {
    alert(message);
  }

  render() {
    return (
      <View>
        <TouchableOpacity>
          <Text onPress={this.toNative}>{"Go to Native"}</Text>
        </TouchableOpacity>
        <TouchableOpacity>
          <Text onPress={this.toReact}>{"Click Me callReactNative"}</Text>
        </TouchableOpacity>
      </View>
    )
  }
}

const nativeApp = new App();
BatchedBridge.registerCallableModule("receiveModule", nativeApp);

# App.js explanation

1. NativeModules : Used to communicate with native

2. DeviceEventEmitter : used to receive data from native

3. NativeModules.NativeStart.reactToNative();

  Here, NativeStart is an native package,
  reactToNative is an method located in that package 

4. DeviceEventEmitter.addListener('Success', this.eanEvent)

  DeviceEventEmitter is an listener that wait for receive an event with the eventName "Success"
  Once it receives the event call the receiveEvent function


# Android

1. Create the NativeStart java class for communicate with react native :- 

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
    void activityCallBack(@NonNull Callback callback) {
        Activity activity = getCurrentActivity();
        if (activity != null) {
            callback.invoke(activity.getClass().getSimpleName());
        }
    }
}


In this class getback you from android to react native

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
                MainApplication application = (MainApplication) NextPage.this.getApplication();
                ReactNativeHost reactNativeHost = application.getReactNativeHost();
                ReactInstanceManager reactInstanceManager = reactNativeHost.getReactInstanceManager();
                ReactContext reactContext = reactInstanceManager.getCurrentReactContext();

                reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("Success", "Receive Message from NextPage!");
            }
        });
    }
}


2. Create StarterPackage java class as package converter

public class StarterPackage implements ReactPackage {
    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new NativeStart(reactContext));
        return modules;
    }

//    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}


3. Inside MainApplication.java
Add the StarterPackage into PackageList

@Override
protected List<ReactPackage> getPackages() {
  @SuppressWarnings("UnnecessaryLocalVariable")
  List<ReactPackage> packages = new PackageList(this).getPackages();
  // Packages that cannot be autolinked yet can be added manually here, for example:
  // packages.add(new MyReactNativePackage());
  // return packages;
  return  Arrays.<ReactPackage>asList(
    new MainReactPackage(),
    new StarterPackage()
  );
}


# Senario 2:

You have and seperate native and react native project

Step 1: Go to react native root directory and delete all the files inside android folder

Step 2: Copy all the files from your seperate android project and paste it inside the reactnative _rootdirectory/android/

Step 3: Go to react native root directory "npm install"

Step 4: Add this into app gradle file "implementation "com.facebook.react:react-native:+" 

Step 5: Add this into project gradle file into repositories 
        maven {
            // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
            url("$rootDir/../node_modules/react-native/android")
        }
        maven {
            // Android JSC is installed from npm
            url("$rootDir/../node_modules/jsc-android/dist")
        }

Step 6: Set internet permission and devsupport react activity in AndroidManifest.xml
        <uses-permission android:name="android.permission.INTERNET" />
        <activity android:name="com.facebook.react.devsupport.DevSettingsActivity" />

Step 7: Create MainActivity.java

public class MainActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "native_integration";
  }
}


Step 8: Create MainApplication.java

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost =
      new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // packages.add(new MyReactNativePackage());
//          return packages;
          return  Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            new StarterPackage()
          );
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }
      };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
  }

  /**
   * Loads Flipper in React Native templates. Call this in the onCreate method with something like
   * initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
   *
   * @param context
   * @param reactInstanceManager
   */
  private static void initializeFlipper(
      Context context, ReactInstanceManager reactInstanceManager) {
    if (BuildConfig.DEBUG) {
      try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
        Class<?> aClass = Class.forName("com.native_integration.ReactNativeFlipper");
        aClass
            .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
            .invoke(null, context, reactInstanceManager);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }
}

Step 9: Change name inside application in AndroidManifest.xml]
android:name=".MainApplication"


And Now, You can try. Hope this will help you
