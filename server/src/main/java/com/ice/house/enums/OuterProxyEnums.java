package com.ice.house.enums;

/**
 * @author ice
 * @Date 2019/4/19 16:00
 */
public enum OuterProxyEnums {
    ASK_DEVICE_PROXY("/ask/device", "AskDeviceOuterProxyHttpServiceImpl");

    OuterProxyEnums(String path, String className) {
        this.path = path;
        this.className = className;
    }

    private String path;
    private String className;


    public static String getClassName(String path) {
        for (OuterProxyEnums outerProxyEnums : OuterProxyEnums.values()) {
            if (outerProxyEnums.getPath().equals(path)) {
                return outerProxyEnums.getClassName();
            }
        }
        return null;
    }

    public String getPath() {
        return path;
    }

    public String getClassName() {
        return className;
    }
}
