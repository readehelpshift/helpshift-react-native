import React from 'react';
import { NativeModules, requireNativeComponent } from 'react-native';
import PropTypes from 'prop-types';

class Helpshift extends React.Component {
  render() {
    console.log(this.props)
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
      identifier: PropTypes.string,
      email: PropTypes.string,
      name: PropTypes.string,
      authToken: PropTypes.string
    })
  }).isRequired
};

const { RNHelpshift } = NativeModules;

Helpshift.init = (apiKey, domain, appId) => RNHelpshift.init(apiKey, domain, appId);

Helpshift.login = user => RNHelpshift.login(user);

Helpshift.logout = user => RNHelpshift.logout();

Helpshift.showConversation = () => RNHelpshift.showConversation();

Helpshift.showFAQs = () => RNHelpshift.showFAQs();

Helpshift.showConversationWithCIFs = cifs => RNHelpshift.showConversationWithCIFs(cifs);

Helpshift.showFAQsWithCIFs = cifs => RNHelpshift.showFAQsWithCIFs(cifs);

var RNTHelpshift = requireNativeComponent('RNTHelpshift', Helpshift);

export default Helpshift;