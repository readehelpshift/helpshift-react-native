
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

RCT_EXPORT_METHOD(login:(NSString *)identifier)
{
    HelpshiftUserBuilder *userBuilder = [[HelpshiftUserBuilder alloc] initWithIdentifier:identifier andEmail:nil];
    [HelpshiftCore login:userBuilder.build];
}

RCT_EXPORT_METHOD(loginWithEmail:(NSString *)identifier email:(NSString *)email)
{
    HelpshiftUserBuilder *userBuilder = [[HelpshiftUserBuilder alloc] initWithIdentifier:identifier andEmail:email];
    [HelpshiftCore login:userBuilder.build];
}

RCT_EXPORT_METHOD(loginWithName:(NSString *)identifier name:(NSString *)name)
{
    HelpshiftUserBuilder *userBuilder = [[HelpshiftUserBuilder alloc] initWithIdentifier:identifier andEmail:nil];
    userBuilder.name = name;
    [HelpshiftCore login:userBuilder.build];
}

RCT_EXPORT_METHOD(loginWithEmailAndName:(NSString *)identifier email:(NSString *)email name:(NSString *)name)
{
    HelpshiftUserBuilder *userBuilder = [[HelpshiftUserBuilder alloc] initWithIdentifier:identifier andEmail:email];
    userBuilder.name = name;
    [HelpshiftCore login:userBuilder.build];
}

RCT_EXPORT_METHOD(logout)
{
    [HelpshiftCore logout];
}

RCT_EXPORT_METHOD(showConversation)
{
    UIViewController *rootController = UIApplication.sharedApplication.delegate.window.rootViewController;
    [HelpshiftSupport showConversation:rootController withConfig: nil];
}

RCT_EXPORT_METHOD(showConversationWithCIFs:(NSDictionary *)cifs)
{
    HelpshiftAPIConfigBuilder *builder = [[HelpshiftAPIConfigBuilder alloc] init];
    builder.customIssueFields = cifs;
    HelpshiftAPIConfig *apiConfig = [builder build];
    UIViewController *rootController = UIApplication.sharedApplication.delegate.window.rootViewController;
    [HelpshiftSupport showConversation:rootController withConfig: apiConfig];
}

RCT_EXPORT_METHOD(showFAQs)
{
    UIViewController *rootController = UIApplication.sharedApplication.delegate.window.rootViewController;
    [HelpshiftSupport showFAQs:rootController withConfig:nil];
}

RCT_EXPORT_METHOD(showFAQsWithCIFs:(NSDictionary *)cifs)
{
    HelpshiftAPIConfigBuilder *builder = [[HelpshiftAPIConfigBuilder alloc] init];
    builder.customIssueFields = cifs;
    HelpshiftAPIConfig *apiConfig = [builder build];
    UIViewController *rootController = UIApplication.sharedApplication.delegate.window.rootViewController;
    [HelpshiftSupport showFAQs:rootController withConfig:apiConfig];
}
@end
  
