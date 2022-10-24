
#import "RCTLog.h"
#import "RCTViewManager.h"
#import "RCTBridgeModule.h"
#import "RCTEventEmitter.h"

#import "RNHelpshift.h"

@import HelpshiftX;

@implementation RNHelpshift

-(id) init {
    self = [super init];
    [[Helpshift sharedInstance] setDelegate:self];
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
    NSDictionary *config = @{};
    [Helpshift installWithPlatformId:appId
                              domain:domain
                              config:config];
}

RCT_EXPORT_METHOD(showConversation)
{
    NSDictionary *configDictionary = @{};
    UIViewController *rootController = UIApplication.sharedApplication.delegate.window.rootViewController;
    [Helpshift showConversationWith:rootController config:configDictionary];
}

RCT_EXPORT_METHOD(showFAQs)
{
    NSDictionary *configDictionary = @{};
    UIViewController *rootController = UIApplication.sharedApplication.delegate.window.rootViewController;
    [Helpshift showFAQsWith:rootController config:configDictionary];
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

@end



@interface RNTHelpshiftManager : RCTViewManager
@property(nonatomic,strong) UIView* helpshiftView;
@end

@implementation RNTHelpshiftManager

RCT_EXPORT_MODULE(RNTHelpshift)

- (UIView *)view
{
    UIView *view = [[UIView alloc] init];
    self.helpshiftView = view;
    return view;
}

@end
