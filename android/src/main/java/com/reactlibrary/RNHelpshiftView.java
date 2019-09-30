package com.reactlibrary;

import android.app.Application;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.helpshift.Core;
import com.helpshift.HelpshiftUser;
import com.helpshift.exceptions.InstallException;
import com.helpshift.support.ApiConfig;
import com.helpshift.support.Support;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class RNHelpshiftView extends SimpleViewManager<FrameLayout> {

    public static final String REACT_CLASS = "RNTHelpshift";

    private ThemedReactContext mReactContext;
    private Application mApplication;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public FrameLayout createViewInstance(ThemedReactContext context) {
        FrameLayout frameLayout = new FrameLayout(context);

        mReactContext = context;

        mApplication = (Application)context.getApplicationContext();

        return frameLayout;
    }

    @ReactProp(name = "config")
    public void setConfig(FrameLayout frameLayout, ReadableMap config) throws InstallException {
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

        // Fragment helpshiftFragment = Support.getConversationFragment(mReactContext.getCurrentActivity());
        // mReactContext.getCurrentActivity().getFragmentManager().beginTransaction().add(frameLayout.getId(), helpshiftFragment).commit();

         LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 0);
         frameLayout.setLayoutParams(lp);
         frameLayout.setBackgroundColor(Color.parseColor("purple"));
    }

    private void login(ReadableMap user){
        HelpshiftUser userBuilder;
        String email = user.hasKey("email") ? user.getString("email") : null;
        String indentifier = user.hasKey("indentifier") ? user.getString("indentifier") : null;
        if(user.hasKey("name") && user.hasKey("authToken")) {
            userBuilder = new HelpshiftUser.Builder(indentifier, email)
                    .setName(user.getString("name"))
                    .setAuthToken(user.getString("authToken"))
                    .build();
        } else if (user.hasKey("name")) {
            userBuilder = new HelpshiftUser.Builder(indentifier, email)
                    .setName(user.getString("name"))
                    .build();
        } else if (user.hasKey("authToken")) {
            userBuilder = new HelpshiftUser.Builder(indentifier, email)
                    .setAuthToken(user.getString("authToken"))
                    .build();
        } else {
            userBuilder = new HelpshiftUser.Builder(indentifier, email).build();
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

}