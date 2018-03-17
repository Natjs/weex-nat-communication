package com.instapp.nat.weex.plugin.Communication;

import android.Manifest;
import android.app.Activity;

import com.alibaba.weex.plugin.annotation.WeexModule;
import com.instapp.nat.communication.CommunicationModule;
import com.instapp.nat.communication.Constant;
import com.instapp.nat.communication.ModuleResultListener;
import com.instapp.nat.communication.Util;
import com.instapp.nat.permission.PermissionChecker;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Acathur on 17/2/16.
 * Copyright (c) 2017 Instapp. All rights reserved.
 */

@WeexModule(name = "nat/communication")
public class Communication extends WXModule {

    String mCallNumber;
    JSCallback mCallCallback;

    String lang = Locale.getDefault().getLanguage();
    Boolean isChinese = lang.startsWith("zh");

    @JSMethod
    public void call(String number, final JSCallback jsCallback){
        boolean permAllow = PermissionChecker.lacksPermission(mWXSDKInstance.getContext(), Manifest.permission.CALL_PHONE);

        if (permAllow) {
            HashMap<String, String> dialog = new HashMap<>();
            if (isChinese) {
                dialog.put("title", "权限申请");
                dialog.put("message", "请允许应用拨打电话");
            } else {
                dialog.put("title", "Permission Request");
                dialog.put("message", "Please allow the app to make calls");
            }
            
            mCallNumber = number;
            mCallCallback = jsCallback;

            PermissionChecker.requestPermissions((Activity) mWXSDKInstance.getContext(), dialog, new com.instapp.nat.permission.ModuleResultListener() {
                @Override
                public void onResult(Object o) {
                    if ((boolean)o == true) jsCallback.invoke(Util.getError(Constant.CALL_PHONE_PERMISSION_DENIED, Constant.CALL_PHONE_PERMISSION_DENIED_CODE));
                }
            }, Constant.CALL_PHONE_PERMISSION_REQUEST_CODE, Manifest.permission.CALL_PHONE);
        } else {
            realCall(number, jsCallback);
        }
    }

    @JSMethod
    public void mail(String[] tos, HashMap<String, String> params, final JSCallback jsCallback){
        CommunicationModule.getInstance(mWXSDKInstance.getContext()).mail(tos, params, new ModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }

    @JSMethod
    public void sms(String[] tos, String text, final JSCallback jsCallback){
        CommunicationModule.getInstance(mWXSDKInstance.getContext()).sms(tos, text, new ModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.CALL_PHONE_PERMISSION_REQUEST_CODE) {
            if (PermissionChecker.hasAllPermissionsGranted(grantResults)) {
                realCall(mCallNumber, mCallCallback);
            } else {
                mCallCallback.invoke(Util.getError(Constant.CALL_PHONE_PERMISSION_DENIED, Constant.CALL_PHONE_PERMISSION_DENIED_CODE));
            }
        }
    }

    public void realCall(String number, final JSCallback jsCallback){
        CommunicationModule.getInstance(mWXSDKInstance.getContext()).call(number, new ModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }
}
