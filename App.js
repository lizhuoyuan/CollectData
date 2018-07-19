/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, {Component} from 'react';
import {
    Platform,
    StyleSheet,
    Text,
    View,
    BackHandler,
    ToastAndroid
} from 'react-native';

import GetCallRecords from 'Util/NativeModules';
import SplashScreen from 'react-native-splash-screen';
import global from "./js/global";


const instructions = Platform.select({
    ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
    android: 'Double tap R  ,\n' +
    'Shake or press menu button for dev menu',
});

type Props = {};
export default class App extends Component<Props> {

    componentDidMount() {
        SplashScreen.hide();
        BackHandler.addEventListener('hardwareBackPress', this.onBackPressed);
    }

    componentWillUnmount() {
        BackHandler.removeEventListener('hardwareBackPress', this.onBackPressed);
    }

    onBackPressed = () => {
        if (this.lastBackPressTime && this.lastBackPressTime + 2000 > Date.now()) {
            //2秒内按过返回键
            //BackHandler.exitApp();
            return false;
        }
        this.lastBackPressTime = Date.now();
        ToastAndroid.show('再按一次退出应用', ToastAndroid.SHORT);
        return true;
    };

    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.welcome}>
                    {global.hello}
                </Text>

                <Text style={styles.instructions}>
                    {instructions}
                </Text>

                <Text onPress={() => {
                    GetCallRecords.getCallRecords((infos) => {
                        console.log(infos);
                        alert(`获取到${infos.length}条通话记录`)
                    });
                }}>获取通话记录</Text>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});
