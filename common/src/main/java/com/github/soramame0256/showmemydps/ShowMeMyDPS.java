package com.github.soramame0256.showmemydps;

public class ShowMeMyDPS {
    public static final String MOD_ID = "showmemydps";
    public static Feature featureInstance;
    // We can use this if we don't want to use DeferredRegister
    public static void init() {
        featureInstance = new Feature();
    }
}
