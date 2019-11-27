package com.reactlibrary;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.view.ReactViewGroup;
import com.helpshift.Core;
import com.helpshift.HelpshiftUser;
import com.helpshift.delegate.AuthenticationFailureReason;
import com.helpshift.exceptions.InstallException;
import com.helpshift.support.ApiConfig;
import com.helpshift.support.Support;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class RNHelpshiftView extends SimpleViewManager<ReactViewGroup> implements Support.Delegate {

    public static final String REACT_CLASS = "RNTHelpshift";

    private ThemedReactContext mReactContext;
    private Application mApplication;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public ReactViewGroup createViewInstance(ThemedReactContext context) {
        ReactViewGroup reactView = new ReactViewGroup(context);

        mReactContext = context;

        mApplication = (Application)context.getApplicationContext();

        return reactView;
    }

    @ReactProp(name = "config")
    public void setConfig(ReactViewGroup reactView, ReadableMap config) throws InstallException {
        Support.setDelegate(this);
        Core.init(Support.getInstance());
        Core.install(mApplication,  config.getString("apiKey"),  config.getString("domain"),  config.getString("appId"));

        if (config.hasKey("user")) {
            this.login(config.getMap("user"));
        }


        // TODO: USE FRAGMENT INSTEAD
        if (config.hasKey("cifs")) {
            ApiConfig apiConfig = new ApiConfig.Builder().setCustomIssueFields(getCustomIssueFields(config.getMap("cifs"))).build();
            Support.showConversation(mReactContext.getCurrentActivity(), apiConfig);
        } else {
            Support.showConversation(mReactContext.getCurrentActivity());
        }

//         Activity activity = mReactContext.getCurrentActivity();
//         FragmentManager fragmentManager = ((AppCompatActivity)activity).getSupportFragmentManager();
//
//         Fragment helpshiftFragment;
//         if (config.hasKey("cifs")) {
//             ApiConfig apiConfig = new ApiConfig.Builder().setCustomIssueFields(getCustomIssueFields(config.getMap("cifs"))).build();
//             helpshiftFragment = Support.getConversationFragment(activity, apiConfig);
//             // helpshiftFragment = Support.getFAQsFragment(activity, apiConfig);
//         } else {
//             helpshiftFragment = Support.getConversationFragment(activity);
//             // helpshiftFragment = Support.getFAQsFragment(activity);
//         }
//
//         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//         fragmentTransaction.add(reactView.getId(), helpshiftFragment);
//         fragmentTransaction.commit();
    }

    private void login(ReadableMap user){
        HelpshiftUser userBuilder;
        String email = user.hasKey("email") ? user.getString("email") : null;
        String identifier = user.hasKey("identifier") ? user.getString("identifier") : null;
        if(user.hasKey("name") && user.hasKey("authToken")) {
            userBuilder = new HelpshiftUser.Builder(identifier, email)
                    .setName(user.getString("name"))
                    .setAuthToken(user.getString("authToken"))
                    .build();
        } else if (user.hasKey("name")) {
            userBuilder = new HelpshiftUser.Builder(identifier, email)
                    .setName(user.getString("name"))
                    .build();
        } else if (user.hasKey("authToken")) {
            userBuilder = new HelpshiftUser.Builder(identifier, email)
                    .setAuthToken(user.getString("authToken"))
                    .build();
        } else {
            userBuilder = new HelpshiftUser.Builder(identifier, email).build();
        }

        Core.login(userBuilder);
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

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @Override
    public void sessionBegan() {
        Log.d("Helpshift", "sessionBegan");
        sendEvent(mReactContext, "Helpshift/SessionBegan", Arguments.createMap());
    }

    @Override
    public void sessionEnded() {
        Log.d("Helpshift", "sessionEnded");
        sendEvent(mReactContext, "Helpshift/SessionEnded", Arguments.createMap());
    }

    @Override
    public void newConversationStarted(String newConversationMessage) {
        Log.d("Helpshift", "newConversationStarted");
        WritableMap params = Arguments.createMap();
        params.putString("newConversationMessage", newConversationMessage);
        sendEvent(mReactContext, "Helpshift/NewConversationStarted", params);
    }

    @Override
    public void conversationEnded() {
        Log.d("Helpshift", "conversationEnded");
        sendEvent(mReactContext, "Helpshift/ConversationEnded", Arguments.createMap());
    }

    @Override
    public void userRepliedToConversation(String newMessage) {
        Log.d("Helpshift", "newConversationStarted");
        WritableMap params = Arguments.createMap();
        params.putString("newMessage", newMessage);
        sendEvent(mReactContext, "Helpshift/UserRepliedToConversation", params);
    }

    @Override
    public void userCompletedCustomerSatisfactionSurvey(int rating, String feedback) {
        Log.d("Helpshift", "userCompletedCustomerSatisfactionSurvey");
        WritableMap params = Arguments.createMap();
        params.putInt("rating", rating);
        params.putString("feedback", feedback);
        sendEvent(mReactContext, "Helpshift/UserCompletedCustomerSatisfactionSurvey", params);
    }


    //TODO: determine if File can be sent by React Native bridge
    @Override
    public void displayAttachmentFile(Uri attachmentFile) { }

    // TODO: determine if File can be sent by React Native bridge
    @Override
    public void displayAttachmentFile(File attachmentFile) {}

    @Override
    public void didReceiveNotification(int newMessagesCount) {
        Log.d("Helpshift", "didReceiveNotification");
        WritableMap params = Arguments.createMap();
        params.putInt("newMessagesCount", newMessagesCount);
        sendEvent(mReactContext, "Helpshift/DidReceiveNotification", params);
    }

    @Override
    public void authenticationFailed(HelpshiftUser user, AuthenticationFailureReason reason) {
        Log.d("Helpshift", "authenticationFailed");
        WritableMap params = Arguments.createMap();
        params.putString("user", user.toString());
        params.putString("reason", reason.toString());
        sendEvent(mReactContext, "Helpshift/AuthenticationFailed", params);
    }

}