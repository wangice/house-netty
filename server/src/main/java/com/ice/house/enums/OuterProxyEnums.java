package com.ice.house.enums;

/**
 * @author ice
 * @Date 2019/4/19 16:00
 */
public enum OuterProxyEnums {
    ASK_DEVICE_PROXY("GET", "/ask/device", "AskDeviceOuterProxyHttpServiceImpl");

    OuterProxyEnums(String methon, String path, String className) {
        this.methon = methon;
        this.path = path;
        this.className = className;
    }

    private String methon;
    private String path;
    private String className;


    public static String getClassName(String methon, String path) {
        for (OuterProxyEnums outerProxyEnums : OuterProxyEnums.values()) {
            if (outerProxyEnums.getMethon().equals("GET") && outerProxyEnums.getPath().equals(path)) {
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

    public String getMethon() {
        return methon;
    }
}
