package com.nwayrtc.NWayRtcDemo;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.nwayrtc.sdk.NWayRtc;

import cn.jpush.android.api.JPushInterface;

public class InitApplication extends Application {
    public String registrationID;
    private SharedPreferences sharedPref;
    String phoneNumber = null;
    String password = null;
    String sip_server = null;
    String transport_type = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //if (!PhoneService.isready()) {
          //  PhoneService.instance().initSDK(this, 4, "0.0.0.0", 5060);
        //}
        Log.e("xxx", "开始初始化!");
        /*开启debug*/
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        // 如果不想设置别名的话就不调用这个方法
        initJgAlias();

        /*sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        phoneNumber = sharedPref.getString("sip_account_username", "");
        password = sharedPref.getString("sip_account_password", "");
        sip_server = sharedPref.getString("sip_account_domain", "");
        transport_type = sharedPref.getString("sip_account_transport", "tcp");

        AccountConfig accountConfig = new AccountConfig();
        accountConfig.username = phoneNumber;
        accountConfig.password = password;
        accountConfig.server = sip_server;
        accountConfig.trans_type = transport_type;

        if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(sip_server)) {
            PhoneService.instance().registerSipAccount(accountConfig);
        }*/
    }

    /**
     * 程序停止，取消连接
     */
    @Override
    public void onTerminate() {
        // 在这里可以断开连接
        super.onTerminate();
    }

    /**
     * 初始化极光推送的别名
     */
    public void initJgAlias() {
        //在jpush上设置别名,第一个参数就是applicationcontext，第二个随意，别重复就行，第三个就是你的别名
        JPushInterface.setAlias(this.getApplicationContext(), 57, "testAlias");
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public void registerSipAccount() {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        phoneNumber = sharedPref.getString("sip_account_username", "");
        password = sharedPref.getString("sip_account_password", "");
        sip_server = sharedPref.getString("sip_account_domain", "");
        transport_type = sharedPref.getString("sip_account_transport", "tcp");

        if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(sip_server)) {
            NWayRtc.login(sip_server, phoneNumber, password, phoneNumber, 3600,
                    false, null, null, null);
        }
    }
}