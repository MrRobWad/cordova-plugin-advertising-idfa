#import "AdvertisingIDFA.h"
#import <Cordova/CDV.h>

@implementation AdvertisingIDFA


// Returns advertisingIdentifier as string
// Will return error on iOS < 6.0 since AdSupport framwork doesn't exist
- (void)getAdId:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;

        // throw error if on iOS < 6.0
        if (NSClassFromString(@"ASIdentifierManager")) {

            NSString *advertiserID = [[(id)[NSClassFromString(@"ASIdentifierManager") sharedManager] advertisingIdentifier] UUIDString];
            
            // have to handle iOS bug where 00000000-0000-0000-0000-000000000000 may be returned on iOS 6.0
            if (advertiserID != nil && [advertiserID length] > 0 && ![advertiserID isEqualToString:@"00000000-0000-0000-0000-000000000000"]) {
                pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:advertiserID];
            }
            
            else {
                pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
            }
        }
        
        else {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        }
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    
    }];
}

// Returns advertisingTrackingEnabled as boolean
// Will return error on iOS < 6.0 since AdSupport framwork doesn't exist
- (void)isTrackingEnabled:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        
        if (NSClassFromString(@"ASIdentifierManager")) {

            BOOL enabled = [[NSClassFromString(@"ASIdentifierManager") sharedManager] isAdvertisingTrackingEnabled];
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:enabled];

        } else {

            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];

        }
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        
    }];
}

// Returns dictionary containing idfa and tracking enabled bool
// Will return error on iOS < 6.0 since AdSupport framwork doesn't exist
- (void)getAdInfo:(CDVInvokedUrlCommand*)command
{
    [self.commandDelegate runInBackground:^{
        CDVPluginResult* pluginResult = nil;
        
        // throw error if on iOS < 6.0
        if (NSClassFromString(@"ASIdentifierManager")) {
        
            NSString *advertiserID = [[(id)[NSClassFromString(@"ASIdentifierManager") sharedManager] advertisingIdentifier] UUIDString];
            
            BOOL enabled = [[NSClassFromString(@"ASIdentifierManager") sharedManager] isAdvertisingTrackingEnabled];
            
            // have to handle iOS bug where 00000000-0000-0000-0000-000000000000 may be returned on iOS 6.0
            // set advertiserID to an empty string 
            if ([advertiserID isEqualToString:@"00000000-0000-0000-0000-000000000000"]) {
                advertiserID = @"";
            }
            
            NSDictionary *adData = @{
                                     @"adId" : advertiserID,
                                     @"trackingEnabled" : [NSNumber numberWithBool:enabled]
            };
        
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:adData];
        } else {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        }
        
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        
    }];
}
@end