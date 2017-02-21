package com.nat.weex;

import android.Manifest;
import android.app.Activity;

import com.nat.communication.HLCommModule;
import com.nat.communication.HLConstant;
import com.nat.communication.HLModuleResultListener;
import com.nat.communication.HLUtil;
import com.nat.permission.PermissionChecker;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;

/**
 * Created by Daniel on 17/2/16.
 * Copyright (c) 2017 Nat. All rights reserved.
 */

public class Communication extends WXModule{

    JSCallback mCallCallback;
    String mCallNumber;

    @JSMethod
    public void call(String number, final JSCallback jsCallback){
        if (PermissionChecker.lacksPermission(mWXSDKInstance.getContext(), Manifest.permission.CALL_PHONE)) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("title", "电话权限请求");
            hashMap.put("message", "请允许电话权限");
            mCallCallback = jsCallback;
            mCallNumber = number;
            PermissionChecker.requestPermissions((Activity) mWXSDKInstance.getContext(), hashMap, new com.nat.permission.HLModuleResultListener() {
                @Override
                public void onResult(Object o) {
                    if ((boolean)o == true) jsCallback.invoke(HLUtil.getError(HLConstant.CALL_PHONE_PERMISSION_DENIED, HLConstant.CALL_PHONE_PERMISSION_DENIED_CODE));
                }
            }, HLConstant.CALL_PHONE_PERMISSION_REQUEST_CODE, Manifest.permission.CALL_PHONE);
        } else {
            realCall(number, jsCallback);
        }
    }

    @JSMethod
    public void mail(String[] tos, HashMap<String, String> params, final JSCallback jsCallback){
        HLCommModule.getInstance(mWXSDKInstance.getContext()).mail(tos, params, new HLModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }

    @JSMethod
    public void sms(String[] tos, String text, final JSCallback jsCallback){
        HLCommModule.getInstance(mWXSDKInstance.getContext()).sms(tos, text, new HLModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == HLConstant.CALL_PHONE_PERMISSION_REQUEST_CODE) {
            if (PermissionChecker.hasAllPermissionsGranted(grantResults)) {
                realCall(mCallNumber, mCallCallback);
            } else {
                mCallCallback.invoke(HLUtil.getError(HLConstant.CALL_PHONE_PERMISSION_DENIED, HLConstant.CALL_PHONE_PERMISSION_DENIED_CODE));
            }
        }
    }

    public void realCall(String number, final JSCallback jsCallback){
        HLCommModule.getInstance(mWXSDKInstance.getContext()).call(number, new HLModuleResultListener() {
            @Override
            public void onResult(Object o) {
                jsCallback.invoke(o);
            }
        });
    }
}
