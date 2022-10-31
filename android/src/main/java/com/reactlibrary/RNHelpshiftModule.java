
package com.helpshift.reactlibrary;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

import com.helpshift.Helpshift;
import com.helpshift.HelpshiftEvent;
import com.helpshift.HelpshiftEventsListener;
import com.helpshift.HelpshiftAuthenticationFailureReason;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

public class RNHelpshiftModule extends ReactContextBaseJavaModule {

    private final Application app;
    private ReactApplicationContext mReactContext;
    private Handler countSuccessHandler;
    private Handler countErrorHandler;

    public RNHelpshiftModule(ReactApplicationContext reactContext)  {
        super(reactContext);

        mReactContext= reactContext;

        this.app = (Application)reactContext.getApplicationContext();
    }
     
    public String getName() {
        return "RNHelpshift";
    }

    @ReactMethod
    public void init(String key, String domain, String appid) throws Exception {
        Map<String, Object> params = new HashMap<>(); // a mettre dans les parametres plus tard
        Helpshift.install(this.app, appid, domain, params);
        // Helpshift.setDelegate(this);
        // Core.init(Support.getInstance());
        // Core.install(this.app, key, domain, appid);
    }

    @ReactMethod
    public void showConversation(ReadableMap customMeta) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customMetadata", getCustomIssueFields(customMeta));
        final Activity activity = getCurrentActivity();
        Helpshift.showConversation(activity, params);
    }

    // @ReactMethod
    // public void showConversationWithCIFs(ReadableMap cifs){
    //     final Activity activity = getCurrentActivity();
    //     ApiConfig apiConfig = new ApiConfig.Builder().setCustomIssueFields(getCustomIssueFields(cifs)).build();
    //     Support.showConversation(activity, apiConfig);
    // }

    @ReactMethod
    public void showFAQs(HashMap<String, String> customMetadata) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customMetadata", customMetadata);
        final Activity activity = getCurrentActivity();
        Helpshift.showFAQs(activity, params);
    }

    // @ReactMethod
    // public void showFAQsWithCIFs(ReadableMap cifs){
    //     final Activity activity = getCurrentActivity();
    //     ApiConfig apiConfig = new ApiConfig.Builder().setCustomIssueFields(getCustomIssueFields(cifs)).build();
    //     Helpshift.showFAQs(activity, apiConfig);
    // }

    // @ReactMethod
    // public void requestUnreadMessagesCount(){

    //     // TODO: Is the correct place to create these?
    //     countErrorHandler = new Handler() {
    //         public void handleMessage(Message msg) {
    //             super.handleMessage(msg);
    //         }
    //     };

    //     countSuccessHandler = new Handler() {
    //         public void handleMessage(Message msg) {
    //             super.handleMessage(msg);
    //             Bundle countData = (Bundle) msg.obj;
    //             Integer count = countData.getInt("value");
    //             Map<String, Object> params = new HashMap<>();
    //             params.putInt("count", count);
    //             sendEvent(mReactContext, "didReceiveUnreadMessagesCount", params);
    //         }
    //     };

    //     Helpshift.getNotificationCount(countSuccessHandler, countErrorHandler);
    // }

    @ReactMethod
    public void requestUnreadMessagesCount(){

        // TODO: Is the correct place to create these?
        Helpshift.requestUnreadMessageCount(true);

        Helpshift.setHelpshiftEventsListener(new HelpshiftEventsListener() {
        @Override
        public void onEventOccurred(String eventName, Map<String, Object> data) {
            switch(eventName){
            case HelpshiftEvent.RECEIVED_UNREAD_MESSAGE_COUNT:
                int count = (int) data.get(HelpshiftEvent.DATA_MESSAGE_COUNT);
                boolean fromCache = (boolean) data.get(HelpshiftEvent.DATA_MESSAGE_COUNT_FROM_CACHE);
                Map<String, Object> params = new HashMap<>();
                params.put("count", count);
                sendEvent(mReactContext, "Helpshift/DidReceiveUnreadMessagesCount", params);
            }
        }
        public void onUserAuthenticationFailure(HelpshiftAuthenticationFailureReason reason) {}
    });
    }

    private void sendEvent(ReactContext reactContext, String eventName, Map<String, Object> params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    private HashMap<String, String> getCustomIssueFields(ReadableMap cifs) {
        ReadableMapKeySetIterator iterator = cifs.keySetIterator();
        HashMap<String, String> customIssueFields = new HashMap<>();

        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            String key2 = cifs.getString(key);
            customIssueFields.put(key, key2);
        }

        return customIssueFields;
    }

    public void sessionBegan() {
        Log.d("Helpshift", "sessionBegan");
        sendEvent(mReactContext, "Helpshift/SessionBegan", new HashMap<>());
    }

     
    public void sessionEnded() {
        Log.d("Helpshift", "sessionEnded");
        sendEvent(mReactContext, "Helpshift/SessionEnded", new HashMap<>());
    }

    //  
    // public void newConversationStarted(String newConversationMessage) {
    //     Log.d("Helpshift", "newConversationStarted");
    //     Map<String, Object> params = new HashMap<>();
    //     params.putString("newConversationMessage", newConversationMessage);
    //     sendEvent(mReactContext, "Helpshift/NewConversationStarted", params);
    // }

     
    public void conversationEnded() {
        Log.d("Helpshift", "conversationEnded");
        sendEvent(mReactContext, "Helpshift/ConversationEnded", new HashMap<>());
    }

    //  
    // public void userRepliedToConversation(String newMessage) {
    //     Log.d("Helpshift", "userRepliedToConversation");
    //     Map<String, Object> params = new HashMap<>();
    //     params.putString("newMessage", newMessage);
    //     sendEvent(mReactContext, "Helpshift/UserRepliedToConversation", params);
    // }

    //  
    // public void userCompletedCustomerSatisfactionSurvey(int rating, String feedback) {
    //     Log.d("Helpshift", "userCompletedCustomerSatisfactionSurvey");
    //     Map<String, Object> params = new HashMap<>();
    //     params.putInt("rating", rating);
    //     params.putString("feedback", feedback);
    //     sendEvent(mReactContext, "Helpshift/UserCompletedCustomerSatisfactionSurvey", params);
    // }


    //TODO: determine if File can be sent by React Native bridge
    //  
    // public void displayAttachmentFile(Uri attachmentFile) { }

    // // TODO: determine if File can be sent by React Native bridge
    //  
    // public void displayAttachmentFile(File attachmentFile) {}

    //  
    // public void didReceiveNotification(int newMessagesCount) {
    //     Log.d("Helpshift", "didReceiveNotification");
    //     Map<String, Object> params = new HashMap<>();
    //     params.putInt("newMessagesCount", newMessagesCount);
    //     sendEvent(mReactContext, "Helpshift/DidReceiveNotification", params);
    // }
}