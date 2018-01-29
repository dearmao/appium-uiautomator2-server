package io.appium.uiautomator2.core;


import android.app.UiAutomation;
import android.support.test.uiautomator.Configurator;
import android.view.accessibility.AccessibilityEvent;

import java.util.concurrent.TimeoutException;

import io.appium.uiautomator2.model.AccessibilityScrollData;
import io.appium.uiautomator2.model.AppiumUiAutomatorDriver;
import io.appium.uiautomator2.model.NotificationListener;
import io.appium.uiautomator2.model.Session;
import io.appium.uiautomator2.utils.Logger;

public abstract class EventRegister {

    public static Boolean runAndRegisterScrollEvents (ReturningRunnable<Boolean> runnable, long timeout) {
        // turn off listening to notifications since it interferes with us listening for the scroll
        // event here
        NotificationListener listener = NotificationListener.getInstance();
        boolean notificationListenerActive = listener.isListening;
        if (notificationListenerActive) {
            listener.stop();
        }

        // say we want to listen to only scroll events
        UiAutomation.AccessibilityEventFilter eventFilter = new UiAutomation.AccessibilityEventFilter() {
            @Override
            public boolean accept(AccessibilityEvent event) {
                return event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED;
            }
        };

        AccessibilityEvent event = null;
        try {
            event = UiAutomatorBridge.getInstance().getUiAutomation().executeAndWaitForEvent(runnable,
                    eventFilter, timeout);
            Logger.debug("Retrieved accessibility event for scroll");
        } catch (TimeoutException ignore) {
            Logger.error("Expected to receive a scroll accessibility event but hit the timeout instead");
        }

        Session session = AppiumUiAutomatorDriver.getInstance().getSession();

        if (event == null) {
            session.setLastScrollData(null);
        } else {
            session.setLastScrollData(new AccessibilityScrollData(event));
        }

        // turn back on notification listener if it was active
        if (notificationListenerActive) {
            listener.start();
        }

        // finally, return whatever the runnable set as its result
        return runnable.getResult();
    }

    public static Boolean runAndRegisterScrollEvents (ReturningRunnable<Boolean> runnable) {
        return runAndRegisterScrollEvents(runnable, Configurator.getInstance().getScrollAcknowledgmentTimeout());
    }
}
