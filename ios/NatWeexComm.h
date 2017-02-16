//
//  NatWeexComm.h
//
//  Created by huangyake on 17/1/7.
//  Copyright © 2017 Nat. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <WeexSDK/WeexSDK.h>

@protocol NatCommPro <WXModuleProtocol>
- (void)call:(NSString *)phone :(WXModuleCallback)callback;
- (void)mail:(NSArray *)mail :(NSDictionary*)params :(WXModuleCallback)callback;
- (void)sms:(NSArray *)phone :(NSString *)text :(WXModuleCallback)callback;



@end

@interface NatWeexComm : NSObject<NatCommPro>


@end
