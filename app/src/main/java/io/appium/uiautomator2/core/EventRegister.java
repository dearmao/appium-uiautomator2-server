package io.appium.uiautomator2.core;


import android.app.UiAutomation;
import android.support.test.uiautomator.Configurator;
import android.view.accessibility.AccessibilityEvent;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import io.appium.uiautomator2.model.AccessibilityScrollData;
import io.appium.uiautomator2.model.AppiumUiAutomatorDriver;
import io.appium.uiautomator2.model.NotificationListener;
import io.appium.uiautomator2.model.Session;
import io.appium.uiautomator2.utils.Logger;

public abstract class EventRegister {

    public static int EVENT_COOLDOWN_MS = 750;

    public static Boolean runAndRegisterScrollEvents (ReturningRunnable<Boolean> runnable, long timeout) {
        // turn off listening to notifications since it interferes with us listening for the scroll
        // event here
        NotificationListener listener = NotificationListener.getInstance();

        boolean notificationListenerActive = listener.isListening;
        if (notificationListenerActive) {
            listener.stop();
        }

        // here we set a callback for the accessibility event stream, keeping track of any scroll
        // events we come across
        AccessibilityEvent event = null;
        final ArrayList<AccessibilityEvent> seenEvents = new ArrayList<>();
        UiAutomation automation = UiAutomatorBridge.getInstance().getUiAutomation();
        UiAutomation.OnAccessibilityEventListener onEvent = new UiAutomation.OnAccessibilityEventListener() {
            @Override
            public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
                if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
                    seenEvents.add(accessibilityEvent);
                }
            }
        };
        automation.setOnAccessibilityEventListener(onEvent);

        // actually run the swipe/scroll
        runnable.run();

        // wait a certain amount of time for any accessibility events generated to be caught by
        // our callback, then stop watching for events
        try { Thread.sleep(timeout); } catch (InterruptedException ign) {}
        automation.setOnAccessibilityEventListener(null);

        // if we have caught any events in our net, snatch the last one
        if (seenEvents.size() > 0) {
            event = seenEvents.get(seenEvents.size() - 1); // get the last event we saw
        }

        Session session = AppiumUiAutomatorDriver.getInstance().getSession();

        if (event == null) {
            Logger.debug("Did not retrieve accessibility event for scroll");
            session.setLastScrollData(null);
        } else {
            AccessibilityScrollData data = new AccessibilityScrollData(event);
            Logger.debug("Retrieved accessibility event for scroll: ", data);
            session.setLastScrollData(data);
        }

        // turn back on notification listener if it was active
        if (notificationListenerActive) {
            listener.start();
        }

        // finally, return whatever the runnable set as its result
        return runnable.getResult();
    }

    public static Boolean runAndRegisterScrollEvents (ReturningRunnable<Boolean> runnable) {
        return runAndRegisterScrollEvents(runnable, EVENT_COOLDOWN_MS);
    }
}
