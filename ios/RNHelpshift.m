
#import "RNHelpshift.h"
#import "RCTLog.h"

#import "HelpshiftCore.h"
#import "HelpshiftSupport.h"

@implementation RNHelpshift

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(init:(NSString *)apiKey domain:(NSString *)domain appId:(NSString *)appId)
{
    [HelpshiftCore initializeWithProvider:[HelpshiftSupport sharedInstance]];
    [HelpshiftCore installForApiKey:apiKey domainName:domain appID:appId];   
}

RCT_EXPORT_METHOD(showConversation)
{
  UIViewController *rootController = UIApplication.sharedApplication.delegate.window.rootViewController;
  [HelpshiftSupport showConversation:rootController withOptions:nil];
}

RCT_EXPORT_METHOD(showFAQs)
{
  UIViewController *rootController = UIApplication.sharedApplication.delegate.window.rootViewController;
  [HelpshiftSupport showFAQs:rootController withOptions:nil];
}

@end
  