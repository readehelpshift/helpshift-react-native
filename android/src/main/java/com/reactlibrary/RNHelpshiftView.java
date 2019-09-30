package com.reactlibrary;

import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.helpshift.support.Support;

import androidx.fragment.app.Fragment;

public class RNHelpshiftView extends SimpleViewManager<FrameLayout> {

    public static final String REACT_CLASS = "RNTHelpshift";

    private ThemedReactContext mReactContext;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public FrameLayout createViewInstance(ThemedReactContext context) {
        FrameLayout frameLayout = new FrameLayout(context);

        mReactContext = context;

        return frameLayout;
    }

    @ReactProp(name = "config")
    public void setConfig(FrameLayout frameLayout, ReadableMap config) {
        config.getString("domain");

        //TODO: Init
        //TODO: Login

        // Fragment helpshiftFragment = Support.getConversationFragment(mReactContext.getCurrentActivity());
        // mReactContext.getCurrentActivity().getFragmentManager().beginTransaction().add(frameLayout.getId(), helpshiftFragment).commit();

        // LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(20, 500);
        // frameLayout.setLayoutParams(lp);
        frameLayout.setBackgroundColor(Color.parseColor("purple"));
    }

}