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
    this.eanEvent = this.eanEvent.bind(this);
    DeviceEventEmitter.addListener('Success', this.eanEvent)
  }

  eanEvent(message) {
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
