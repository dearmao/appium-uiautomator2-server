package io.appium.uiautomator2.core;


import android.app.UiAutomation;
import android.support.test.uiautomator.Configurator;
import android.view.accessibility.AccessibilityEvent;

import io.appium.uiautomator2.model.AppiumUiAutomatorDriver;

public abstract class EventRegister {

    public static Boolean runAndRegisterScrollEvents (ReturningRunnable<Boolean> runnable) {
        AccessibilityEvent event = null;
        UiAutomation.AccessibilityEventFilter eventFilter = new UiAutomation.AccessibilityEventFilter() {
            @Override
            public boolean accept(AccessibilityEvent event) {
                return event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED;
            }
        };

        try {
            //wait for AccessibilityEvent filter
            event = UiAutomatorBridge.getInstance().getUiAutomation().executeAndWaitForEvent(runnable,
                    eventFilter, Configurator.getInstance().getScrollAcknowledgmentTimeout());
        } catch (Exception ignore) {}

        if (event != null) {
            AppiumUiAutomatorDriver.getInstance().getSession().setLastScrollData(
                    event.getScrollX(),
                    event.getMaxScrollX(),
                    event.getScrollY(),
                    event.getMaxScrollY(),
                    event.getFromIndex(),
                    event.getToIndex(),
                    event.getItemCount()
            );
        }
        return runnable.getResult();
    }
}
