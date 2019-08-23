
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import com.helpshift.Core;
import com.helpshift.All;
import com.helpshift.InstallConfig;
import com.helpshift.exceptions.InstallException;
import com.helpshift.support.Support;

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
  public void showConversation(){
    final Activity activity = getCurrentActivity();
    Support.showConversation(activity);
  }

  @ReactMethod
  public void showFAQs(){
    final Activity activity = getCurrentActivity();
    Support.showFAQs(activity);
  }

  @Override
  public String getName() {
    return "RNHelpshift";
  }
}