
#import "RCTLog.h"
#import "RCTViewManager.h"
#import "RCTBridgeModule.h"
#import "RCTEventEmitter.h"

#import "RNHelpshift.h"

@import Helpshift;

@implementation RNHelpshift

-(id) init {
    self = [super init];
    [[HelpshiftSupport sharedInstance] setDelegate:self];
    return self;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}


RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(init:(NSString *)apiKey domain:(NSString *)domain appId:(NSString *)appId)
{
    HelpshiftInstallConfigBuilder *installConfigBuilder = [[HelpshiftInstallConfigBuilder alloc] init];
    installConfigBuilder.enableAutomaticThemeSwitching = YES;
    [HelpshiftCore initializeWithProvider:[HelpshiftSupport sharedInstance]];
    [HelpshiftCore installForApiKey:apiKey domainName:domain appID:appId withConfig:installConfigBuilder.build];
}

RCT_EXPORT_METHOD(login:(NSDictionary *)user)
{
    HelpshiftUserBuilder *userBuilder = [[HelpshiftUserBuilder alloc] initWithIdentifier:user[@"identifier"] andEmail:user[@"email"]];
    if (user[@"name"]) userBuilder.name = user[@"name"];
    if (user[@"authToken"]) userBuilder.authToken = user[@"authToken"];
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

RCT_EXPORT_METHOD(requestUnreadMessagesCount)
{
    [HelpshiftSupport requestUnreadMessagesCount:YES];
}

- (NSArray<NSString *> *)supportedEvents
{
    return @[
             @"Helpshift/SessionBegan",
             @"Helpshift/SessionEnded",
             @"Helpshift/NewConversationStarted",
             @"Helpshift/ConversationEnded",
             @"Helpshift/UserRepliedToConversation",
             @"Helpshift/UserCompletedCustomerSatisfactionSurvey",
             @"Helpshift/DidReceiveNotification",
             @"Helpshift/DidReceiveUnreadMessagesCount",
             @"Helpshift/DidReceiveUnreadMessagesCount",
             @"Helpshift/AuthenticationFailed"
    ];
}

- (void) helpshiftSupportSessionHasBegun {
    RCTLog(@"Helpshift/SessionBegan");
    [self sendEventWithName:@"Helpshift/SessionBegan" body:nil];
}

- (void) helpshiftSupportSessionHasEnded {
    RCTLog(@"Helpshift/SessionEnded");
    [self sendEventWithName:@"Helpshift/SessionEnded" body:nil];
}

- (void) newConversationStartedWithMessage:(NSString *)newConversationMessage {
    RCTLog(@"Helpshift/NewConversationStarted: %@", newConversationMessage);
    [self sendEventWithName:@"Helpshift/NewConversationStarted" body:@{@"newConversationMessage": newConversationMessage}];
}

- (void) conversationEnded {
    RCTLog(@"Helpshift/ConversationEnded");
    [self sendEventWithName:@"Helpshift/ConversationEnded" body:nil];
}

- (void) userRepliedToConversationWithMessage:(NSString *)newMessage {
    RCTLog(@"Helpshift/UserRepliedToConversation: %@", newMessage);
    [self sendEventWithName:@"Helpshift/UserRepliedToConversation" body:@{@"newMessage": newMessage}];
}

- (void) userCompletedCustomerSatisfactionSurvey:(NSInteger)rating withFeedback:(NSString *)feedback {
    RCTLog(@"Helpshift/UserCompletedCustomerSatisfactionSurvey rating: %ld feedback: %@", rating, feedback);
    [self sendEventWithName:@"Helpshift/UserCompletedCustomerSatisfactionSurvey" body:@{@"rating": @(rating), @"feedback": feedback}];
}

- (void) didReceiveInAppNotificationWithMessageCount:(NSInteger)count {
    RCTLog(@"Helpshift/DidReceiveNotification: %ld", count);
    [self sendEventWithName:@"Helpshift/DidReceiveNotification" body:@{@"count": @(count)}];
}

- (void)didReceiveUnreadMessagesCount:(NSInteger)count {
    RCTLog(@"Helpshift/DidReceiveUnreadMessagesCount: %ld", count);
    [self sendEventWithName:@"Helpshift/DidReceiveUnreadMessagesCount" body:@{@"count": @(count)}];
}

- (void) authenticationFailedForUser:(HelpshiftUser *)user withReason:(HelpshiftAuthenticationFailureReason)reason {
    RCTLog(@"Helpshift/AuthenticationFailed user: %@", user);
    [self sendEventWithName:@"Helpshift/AuthenticationFailed" body:@{@"user": user}];
}

@end



@interface RNTHelpshiftManager : RCTViewManager
@property(nonatomic,strong) UIView* helpshiftView;
@end

@implementation RNTHelpshiftManager

RCT_EXPORT_MODULE(RNTHelpshift)

RCT_CUSTOM_VIEW_PROPERTY(config, NSDictionary, RNTHelpshiftManager) {
    HelpshiftInstallConfigBuilder *installConfigBuilder = [[HelpshiftInstallConfigBuilder alloc] init];
    installConfigBuilder.enableAutomaticThemeSwitching = YES;
    [HelpshiftCore initializeWithProvider:[HelpshiftSupport sharedInstance]];
    [HelpshiftCore installForApiKey:json[@"apiKey"]
                         domainName:json[@"domain"]
                              appID:json[@"appId"]
                         withConfig:installConfigBuilder.build];

    // Log user in if identified
    if (json[@"user"]) {
        NSDictionary *user = json[@"user"];
        HelpshiftUserBuilder *userBuilder = [[HelpshiftUserBuilder alloc] initWithIdentifier:user[@"identifier"] andEmail:user[@"email"]];
        if (user[@"name"]) userBuilder.name = user[@"name"];
        if (user[@"authToken"]) userBuilder.authToken = user[@"authToken"];
        [HelpshiftCore login:userBuilder.build];
    }
    
    // Get the Helpshift conversation view controller.
    HelpshiftAPIConfigBuilder *builder = [HelpshiftAPIConfigBuilder new];
    // Add CIFS if existing
    if (json[@"cifs"]) builder.customIssueFields = json[@"cifs"];
    [HelpshiftSupport conversationViewControllerWithConfig:[builder build] completion:^(UIViewController *conversationVC) {
        UIViewController *rootController = UIApplication.sharedApplication.delegate.window.rootViewController;
        
        UINavigationController *navController = [[UINavigationController alloc] initWithRootViewController:conversationVC];
        [navController willMoveToParentViewController:rootController];
        
        if (json[@"height"] && json[@"width"]) {
            float height = [json[@"height"] floatValue];
            float width = [json[@"width"] floatValue];
            navController.view.frame = CGRectMake(0, 0, width, height);
        }

        [self.helpshiftView addSubview:navController.view];
        [rootController addChildViewController:navController];
        [navController didMoveToParentViewController:rootController];
    }];
}

- (UIView *)view
{
    UIView *view = [[UIView alloc] init];
    self.helpshiftView = view;
    return view;
}

@end
