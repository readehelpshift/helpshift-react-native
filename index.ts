import React, { useEffect, useRef } from "react";
import {
  NativeModules,
  NativeEventEmitter,
  requireNativeComponent,
} from "react-native";

const { RNHelpshift } = NativeModules;

const HelpshiftEventEmitter = new NativeEventEmitter(RNHelpshift);

export const init = (apiKey: string, domain: string, appId: string) =>
  RNHelpshift.init(apiKey, domain, appId);

// TODO: Rerender Helpshift view on iOS if using <Helpshift/>
export const login = (user: string) => RNHelpshift.login(user);

export const logout = (user: string) => RNHelpshift.logout();

export const showConversation = (cifs: object) =>
  RNHelpshift.showConversation(cifs);

export const showFAQs = () => RNHelpshift.showFAQs();

export const showConversationWithCIFs = (cifs: object) =>
  RNHelpshift.showConversationWithCIFs(cifs);

export const showFAQsWithCIFs = (cifs: object) =>
  RNHelpshift.showFAQsWithCIFs(cifs);

export const requestUnreadMessagesCount = () => {
  RNHelpshift.requestUnreadMessagesCount();
};

export const getHelpshiftEventEmitter = () => {
  return HelpshiftEventEmitter;
};

export const useHelpshiftEventListener = (
  eventName: string,
  handler: Function
) => {
  const savedHandler = useRef<Function | undefined>();

  useEffect(() => {
    savedHandler.current = handler;
  }, [handler]);

  useEffect(
    () => {
      // Make sure element supports addEventListener
      // On
      // Create event listener that calls handler function stored in ref
      const eventListener = (event: Function) => (savedHandler.current as Function)(event);
      // Add event listener
      HelpshiftEventEmitter.addListener(eventName, eventListener);
      // Remove event listener on cleanup
      return () => {
        HelpshiftEventEmitter.addListener(eventName, eventListener);
      };
    },
    [eventName] // Re-run if eventName or element changes
  );
};
