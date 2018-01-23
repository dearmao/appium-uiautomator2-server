package io.appium.uiautomator2.model;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Session {
    public static final String SEND_KEYS_TO_ELEMENT = "sendKeysToElement";
    private String sessionId;
    private ConcurrentMap<String, JSONObject> commandConfiguration;
    private KnownElements knownElements;
    private Map<String, Integer> lastScrollData = new HashMap<>();
    public static Map<String, Object> capabilities = new HashMap<>();

    public Session(String sessionId) {
        this.sessionId = sessionId;
        this.knownElements = new KnownElements();
        this.commandConfiguration = new ConcurrentHashMap<>();
        JSONObject configJsonObject = new JSONObject();
        this.commandConfiguration.put(SEND_KEYS_TO_ELEMENT, configJsonObject);
        NotificationListener.getInstance().start();
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setCommandConfiguration(String command, JSONObject config) {
        if (commandConfiguration.containsKey(command)) {
            commandConfiguration.replace(command, config);
        }
    }

    public KnownElements getKnownElements() {
        return knownElements;
    }

    public JSONObject getCommandConfiguration(String command) {
        return commandConfiguration.get(command);
    }

    public void setLastScrollData(int scrollX, int maxScrollX, int scrollY, int maxScrollY,
                                  int fromIndex, int toIndex, int itemCount) {
        lastScrollData.put("scrollX", scrollX);
        lastScrollData.put("maxScrollX", maxScrollX);
        lastScrollData.put("scrollY", scrollY);
        lastScrollData.put("maxScrollY", maxScrollY);
        lastScrollData.put("fromIndex", fromIndex);
        lastScrollData.put("toIndex", toIndex);
        lastScrollData.put("itemCount", itemCount);
    }

    public Map<String, Integer> getLastScrollData() {
        return lastScrollData;
    }
}
