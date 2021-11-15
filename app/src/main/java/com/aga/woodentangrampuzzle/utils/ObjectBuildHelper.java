package com.aga.woodentangrampuzzle.utils;

import android.util.Log;

public class ObjectBuildHelper {

    //<editor-fold desc="Log Debug Out">
    private static final String APP_TAG = "Tangram: ";
    private static final String dot = ". ";
    private static final String colon = ": ";
//    private static final boolean SHOW_LOG = true;
    private static final boolean SHOW_LOG = false;

    public static void logDebugOut(String object, String message, int param) {
        if (SHOW_LOG) {
            String compiledMessage = object + dot + message + colon + param;
            Log.d(APP_TAG, compiledMessage);
        }
    }

    public static void logDebugOut(String object, String message, float param) {
        if (SHOW_LOG) {
            String compiledMessage = object + dot + message + colon + param;
            Log.d(APP_TAG, compiledMessage);
        }
    }

    public static void logDebugOut(String object, String message, boolean param) {
        if (SHOW_LOG) {
            String compiledMessage = object + dot + message + colon + param;
            Log.d(APP_TAG, compiledMessage);
        }
    }

    public static void logDebugOut(String object, String message, String param) {
        if (SHOW_LOG) {
            String compiledMessage = object + dot + message + colon + param;
            Log.d(APP_TAG, compiledMessage);
        }
    }
    //</editor-fold>
}
