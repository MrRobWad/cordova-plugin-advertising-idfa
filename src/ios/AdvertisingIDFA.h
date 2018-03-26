#import <AdSupport/AdSupport.h>
#import <Cordova/CDV.h>

@interface AdvertisingIDFA : CDVPlugin

- (void)getAdId:(CDVInvokedUrlCommand*)command;

- (void)isTrackingEnabled:(CDVInvokedUrlCommand*)command;

- (void)getAdInfo:(CDVInvokedUrlCommand*)command;

@end