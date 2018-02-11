//
//  WeexNatCommunication.m
//
//  Created by huangyake on 17/1/7.
//  Copyright © 2017 Instapp. All rights reserved.
//

#import "WeexNatCommunication.h"
#import <WeexPluginLoader/WeexPluginLoader.h>
#import <NatCommunication/NatCommunication.h>

@implementation WeexNatCommunication
@synthesize weexInstance;

WX_PlUGIN_EXPORT_MODULE(nat/communication, WeexNatCommunication)
WX_EXPORT_METHOD(@selector(call::))
WX_EXPORT_METHOD(@selector(mail:::))
WX_EXPORT_METHOD(@selector(sms:::))

- (void)call:(NSString *)phone :(WXModuleCallback)callback{
    [[NatCommunication singletonManger] call:phone :^(id error,id result) {
        if (error) {
            if (callback) {
                callback(error);
            }
        } else {
            if (callback) {
                callback(result);
            }
        }
    }];
}


- (void)mail:(NSArray *)mail :(NSDictionary*)params :(WXModuleCallback)callback{
    [[NatCommunication singletonManger] mail:mail :params :^(id error,id result) {
        if (error) {
            if (callback) {
                callback(error);
            }
        } else {
            if (callback) {
                callback(result);
            }
        }
    }];
}

- (void)sms:(NSArray *)phone :(NSString *)text :(WXModuleCallback)callback{
    [[NatCommunication singletonManger] sms:phone :text :^(id error,id result) {
        if (error) {
            if (callback) {
                callback(error);
            }
        } else {
            if (callback) {
                callback(result);
            }
        }
    }];
}

@end
