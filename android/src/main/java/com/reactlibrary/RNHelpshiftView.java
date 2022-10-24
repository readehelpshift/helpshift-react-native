package com.helpshift.reactlibrary;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.view.ReactViewGroup;

import com.helpshift.Helpshift;
import android.content.pm.ActivityInfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class RNHelpshiftView extends ViewGroupManager<ReactViewGroup> {

    public static final String REACT_CLASS = "RNTHelpshift";

    private ThemedReactContext mReactContext;
    private Application mApplication;

     
    public String getName() {
        return REACT_CLASS;
    }

     
    public ReactViewGroup createViewInstance(ThemedReactContext context)  {
        final ReactViewGroup reactView = new ReactViewGroup(context);

        mReactContext = context;

        mApplication = (Application)context.getApplicationContext();

        return reactView;
    }

    @ReactProp(name = "config")
    public void setConfig(final ReactViewGroup reactView, ReadableMap config) {
        // Support.setDelegate(this);
        Map<String, Object> params = new HashMap<>();
        params.put("screenOrientation", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //...
        // Install call
        try {
            Helpshift.install(mApplication,
                    config.getString("appId"),
                    config.getString("domain"),
                    params);
        } catch (Exception e) {
            Log.e("Helpshift", "invalid install credentials : ", e);
        }
        // Core.init(Support.getInstance());
        // InstallConfig installConfig = new InstallConfig.Builder().build();
        // try {
        //     Core.install(mApplication,  config.getString("apiKey"),  config.getString("domain"),  config.getString("appId"), installConfig);
        // } catch (InstallException e) {
        //     Log.e("Helpshift", "invalid install credentials : ", e);
        // }

        // if (config.hasKey("user")) {
        //     this.login(config.getMap("user"));
        // }

        Activity activity = mReactContext.getCurrentActivity();
        final FragmentManager fragmentManager = ((AppCompatActivity)activity).getSupportFragmentManager();
        final Fragment helpshiftFragment;
        Map<String, Object> extras = new HashMap<>();
        extras.put("enableDefaultConversationalFiling", true);

        // if (config.hasKey("cifs")) {
        //     ApiConfig apiConfig = new ApiConfig.Builder().setExtras(extras).setCustomIssueFields(getCustomIssueFields(config.getMap("cifs"))).build();
        //     helpshiftFragment = Support.getConversationFragment(activity, apiConfig);
        // } else {
        //     ApiConfig apiConfig = new ApiConfig.Builder().setExtras(extras).build();
        //     helpshiftFragment = Support.getConversationFragment(activity, apiConfig);
        // }

        // reactView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
             
        //     public void onGlobalLayout() {
        //         fragmentManager.executePendingTransactions();
        //         for (int i = 0; i < reactView.getChildCount(); i++) {
        //             View child = reactView.getChildAt(i);
        //             child.measure(View.MeasureSpec.makeMeasureSpec(reactView.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
        //                     View.MeasureSpec.makeMeasureSpec(reactView.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
        //             child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        //         }
        //     }
        // });

        // fragmentManager.beginTransaction().replace(reactView.getId(), helpshiftFragment).commit();
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

    private void sendEvent(ReactContext reactContext, String eventName, Map<String, Object> params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
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
    //     Map<String, Object> params = new HashMap<>();;
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
    //     Map<String, Object> params = new HashMap<>();;
    //     params.putString("newMessage", newMessage);
    //     sendEvent(mReactContext, "Helpshift/UserRepliedToConversation", params);
    // }

    //  
    // public void userCompletedCustomerSatisfactionSurvey(int rating, String feedback) {
    //     Log.d("Helpshift", "userCompletedCustomerSatisfactionSurvey");
    //     Map<String, Object> params = new HashMap<>();;
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
    //     Map<String, Object> params = new HashMap<>();;
    //     params.putInt("newMessagesCount", newMessagesCount);
    //     sendEvent(mReactContext, "Helpshift/DidReceiveNotification", params);
    // }
}