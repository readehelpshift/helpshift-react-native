
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

import java.util.Map;
import java.util.HashMap;

import com.helpshift.Core;
import com.helpshift.All;
import com.helpshift.exceptions.InstallException;
import com.helpshift.support.Support;
import com.helpshift.HelpshiftUser;
import com.helpshift.support.ApiConfig;

import android.app.Activity;
import android.app.Application;

public class RNHelpshiftModule extends ReactContextBaseJavaModule {

  private final Application app;
  
  public RNHelpshiftModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.app = (Application)reactContext.getApplicationContext();
  }

  @ReactMethod
  public void init(String key, String domain, String appid) throws InstallException {
      Core.init(All.getInstance());
      Core.install(this.app, key, domain, appid);
  }

  @ReactMethod
  public void login(String identifier){
      HelpshiftUser user = new HelpshiftUser.Builder(identifier, null)
              .build();
      Core.login(user);
  }

  @ReactMethod
  public void loginWithEmail(String identifier, String email){
      HelpshiftUser user = new HelpshiftUser.Builder(identifier, email)
              .build();
      Core.login(user);
  }

  @ReactMethod
  public void loginWithName(String identifier, String name){
      HelpshiftUser user = new HelpshiftUser.Builder(identifier, null)
              .setName(name)
              .build();
      Core.login(user);
  }

  @ReactMethod
  public void loginWithEmailAndName(String identifier, String email, String name){
      HelpshiftUser user = new HelpshiftUser.Builder(identifier, email)
              .setName(name)
              .build();
      Core.login(user);
  }

  @ReactMethod
  public void logout(){
      Core.logout();
  }

  @ReactMethod
  public void showConversation(){
    final Activity activity = getCurrentActivity();
    Support.showConversation(activity);
  }

  @ReactMethod
  public void showConversationWithCIFs(ReadableMap cifs){
      final Activity activity = getCurrentActivity();
      ApiConfig apiConfig = new ApiConfig.Builder().setCustomIssueFields(getCustomIssueFields(cifs)).build();
      Support.showConversation(activity, apiConfig);
  }

  @ReactMethod
  public void showFAQs(){
    final Activity activity = getCurrentActivity();
    Support.showFAQs(activity);
  }

  @ReactMethod
  public void showFAQsWithCIFs(ReadableMap cifs){
      final Activity activity = getCurrentActivity();
      ApiConfig apiConfig = new ApiConfig.Builder().setCustomIssueFields(getCustomIssueFields(cifs)).build();
      Support.showFAQs(activity, apiConfig);
  }

  private Map<String, String[]> getCustomIssueFields(ReadableMap cifs) {
      ReadableMapKeySetIterator iterator = cifs.keySetIterator();
      Map<String, String[]> customIssueFields = new HashMap<>();

      while (iterator.hasNextKey()) {
        String key = iterator.nextKey();
        ReadableArray array = cifs.getArray(key);
        customIssueFields.put(key, new String[]{array.getString(0), array.getString(1)});
      }

      return customIssueFields;
  }

  @Override
  public String getName() {
    return "RNHelpshift";
  }
}