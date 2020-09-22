
#import "RCTBridgeModule.h"
#import "RCTEventEmitter.h"

@import Helpshift;

@interface RNHelpshift : RCTEventEmitter <RCTBridgeModule, HelpshiftSupportDelegate>
@end
  
