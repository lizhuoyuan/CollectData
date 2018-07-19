package com.collectdata.collectData;
/**
 * 获取本地通话记录
 */

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;


public class CallRecords extends ReactContextBaseJavaModule {

    public CallRecords(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "GetCallRecords";
    }

    /**
     * 开辟个子线程去获取通话记录
     */
    @ReactMethod
    public void getCallRecords(final Callback successCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                callCor(successCallback);
            }
        }).start();

    }

    @ReactMethod
    public void toastCallRecords() {
        Toast.makeText(getReactApplicationContext(), "原生代码测试", Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取通话记录的方法
     */
    private void callCor(Callback successCallback) {
        WritableArray wt = new WritableNativeArray();
        //List<CallInfo> infos = new ArrayList<CallInfo>();
        ContentResolver resolver = getReactApplicationContext().getContentResolver();
        // uri的写法需要查看源码JB\packages\providers\ContactsProvider\AndroidManifest.xml中内容提供者的授权
        // 从清单文件可知该提供者是CallLogProvider，且通话记录相关操作被封装到了Calls类中
        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] projection = new String[]{
                CallLog.Calls.NUMBER, // 号码
                CallLog.Calls.DATE,   // 日期
                CallLog.Calls.TYPE    // 类型：来电、去电、未接
        };

        Cursor cursor = resolver.query(uri, projection, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {
            String number = cursor.getString(0);   // 号码
            long date = cursor.getLong(1);         // 日期
            int type = cursor.getInt(2);          // 类型：来电、去电、未接

            //infos.add(new CallInfo(number, date, type));

            WritableMap map = Arguments.createMap();
            map.putString("number", number);
            map.putInt("type", type);
            map.putString("date", String.valueOf(date));
            wt.pushMap(map);
            //  wt.pushString(String.valueOf(new CallInfo(number, date, type)));
        }
        cursor.close();
        successCallback.invoke(wt);
    }
}
