
# helpshift-react-native

## Getting started

`$ yarn add helpshift-react-native`

### Mostly automatic installation

`$ react-native link helpshift-react-native`

### Manual installation

#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `helpshift-react-native` and add `RNHelpshift.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNHelpshift.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNHelpshiftPackage;` to the imports at the top of the file
  - Add `new RNHelpshiftPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
    ```
    include ':helpshift-react-native'
    project(':helpshift-react-native').projectDir = new File(rootProject.projectDir,  '../node_modules/helpshift-react-native/android')
    ```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
    ```
   compile project(':helpshift-react-native')
    ```


## Usage

#### Initialize
```javascript
import { Platform } from 'react-native';
import Helpshift from 'helpshift-react-native';

const apiKey = 'HELPSHIFT_API_KEY';
const domain = 'HELPSHIFT_DOMAIN';
const iosAppId = 'HELPSHIFT_IOS_APP_ID';
const androidAppId = 'HELPSHIFT_ANDROID_APP_ID';

const appId = Platform.select({ ios: iosAppId, android: androidAppId })
Helpshift.init(apiKey, domain, appId);
```

#### Login
```javascript
Helpshift.login(identifier)
```

#### Login with Email
```javascript
Helpshift.loginWithEmail(identifier, email)
```

#### Login with Name
```javascript
Helpshift.loginWithName(identifier, name)
```

#### Login with Email and Name
```javascript
Helpshift.loginWithEmailAndName(identifier, email, name)
```

#### Logout
```javascript
Helpshift.logout()
```

#### Show Conversation
```javascript
Helpshift.showConversation()
```

#### Show FAQs
```javascript
Helpshift.showFAQs()
```

#### With Custom Issue Fields (CIFs) ([Documentation](https://support.helpshift.com/kb/article/custom-issue-fields/))
```javascript
const cifs = {
// 'key': ['type', 'value']
   'number_of_rides': ['n', '12'],
   'street': ['sl', '343 sansome'],
   'new_customer': ['b', 'true']
}

// Where data types are mapped like so:
// singleline => sl
// multiline => ml
// number => n
// date => dt
// dropdown => dd
// checkbox => b

Helpshift.showConversationWithCIFs(cifs)
// OR
Helpshift.showFAQsWithCIFs(cifs)
```
  