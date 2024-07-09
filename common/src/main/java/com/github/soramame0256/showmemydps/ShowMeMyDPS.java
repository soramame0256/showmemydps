package com.github.soramame0256.showmemydps;

import java.io.IOException;

public class ShowMeMyDPS {
    public static final String MOD_ID = "showmemydps";

    private static Feature featureInstance;
    private static Data data = null;
    public static void init() {
        try {
            data = new Data();
        } catch (IOException e) {
            System.out.println("Data instance thrown exception! Mod won't save data!");
            e.printStackTrace();
        }
        featureInstance = new Feature(data);
    }

    public static Feature getInstance() {
        return featureInstance;
    }

    public static Data getData() {
        return data;
    }
}
