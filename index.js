import React from 'react';
import { 
  NativeModules,
  NativeEventEmitter,
  requireNativeComponent
} from 'react-native';
import PropTypes from 'prop-types';

class Helpshift extends React.Component {
  render() {
    return <RNTHelpshift {...this.props} />;
  }
}

Helpshift.propTypes = {
  config: PropTypes.shape({
    apiKey: PropTypes.string.isRequired,
    domain: PropTypes.string.isRequired,
    appId: PropTypes.string.isRequired,
    height: PropTypes.number,
    width: PropTypes.number,
    user: PropTypes.shape({
      identifier: PropTypes.string.isRequired,
      email: PropTypes.string,
      name: PropTypes.string,
      authToken: PropTypes.string
    }),
    cifs: PropTypes.object
  }).isRequired
};

const { RNHelpshift } = NativeModules;

const HelpshiftEventEmitter = Helpshift.eventEmitter = new NativeEventEmitter(RNHelpshift);

Helpshift.init = (apiKey, domain, appId) => RNHelpshift.init(apiKey, domain, appId);

// TODO: Rerender Helpshift view on iOS if using <Helpshift/>
Helpshift.login = user => RNHelpshift.login(user)

Helpshift.logout = user => RNHelpshift.logout();

Helpshift.showConversation = () => RNHelpshift.showConversation();

Helpshift.showFAQs = () => RNHelpshift.showFAQs();

Helpshift.showConversationWithCIFs = cifs => RNHelpshift.showConversationWithCIFs(cifs);

Helpshift.showFAQsWithCIFs = cifs => RNHelpshift.showFAQsWithCIFs(cifs);

Helpshift.requestUnreadMessagesCount = () => {
  return new Promise((resolve, reject) => {
    const subscription = HelpshiftEventEmitter.addListener('Helpshift/DidReceiveUnreadMessagesCount',
      ({ count }) => {
        resolve(count);
        subscription.remove();
      }
    );

    RNHelpshift.requestUnreadMessagesCount();
  });
}

var RNTHelpshift = requireNativeComponent('RNTHelpshift', Helpshift);

export default Helpshift;