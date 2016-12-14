package com.blender.grape.resources;

import com.sun.deploy.resources.ResourceManager;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * Created by Tautvydas Ramanauskas IF-4/12
 */
public class DialogResources {
    private static final String UI_RESOURCE_NAME = "com.blender.grape.resources.DialogResources";
    private static final ResourceBundle guiResources = ResourceBundle.getBundle(UI_RESOURCE_NAME);

    // TODO: 16/09/25 handling
    public static String getString(String key) {
        try {
            return guiResources.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    // TODO: 16/09/25 it works smarter and somehow different
    public static String getString(String key, String... values) {
        return ResourceManager.getFormattedMessage(key, values);
//        String string = getString(key);
//        for (int i = 0; i < values.length; i++) {
//            string = string.replace("{" + i + "}", values[i]);
//        }
//        return string;
    }
}
