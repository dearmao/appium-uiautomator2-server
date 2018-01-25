package io.appium.uiautomator2.model;

import java.util.HashMap;

public class AccessibilityScrollData {

    private boolean hasData;
    private int scrollX;
    private int maxScrollX;
    private int scrollY;
    private int maxScrollY;
    private int fromIndex;
    private int toIndex;
    private int itemCount;

    public AccessibilityScrollData(int scrollX, int maxScrollX, int scrollY, int maxScrollY,
                                   int fromIndex, int toIndex, int itemCount) {
        this.scrollX = scrollX;
        this.scrollY = scrollY;
        this.maxScrollX = maxScrollX;
        this.maxScrollY = maxScrollY;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.itemCount = itemCount;
        this.hasData = true;
    }

    public AccessibilityScrollData() {
        clearScrollData();
    }

    public void clearScrollData() {
        this.hasData = false;
    }

    public HashMap<String, Integer> getAsMap () {
        HashMap<String, Integer> map = new HashMap<>();

        if (!hasData) {
            return map;
        }

        map.put("scrollX", scrollX);
        map.put("maxScrollX", maxScrollX);
        map.put("scrollY", scrollY);
        map.put("maxScrollY", maxScrollY);
        map.put("fromIndex", fromIndex);
        map.put("toIndex", toIndex);
        map.put("itemCount", itemCount);

        return map;
    }
}
