package com.miracle.logdog.util;

import android.util.Log;

import com.google.gson.Gson;
import com.miracle.logdog.LogEntity;
import com.miracle.logdog.Logdog;

import java.util.ArrayList;
import java.util.List;

/**
 * miracle
 * 2018/12/4 16:32
 */
public class Utils {

    private static final String TAG = "Logdog";

    public static boolean isTrimEmpty(final String s) {
        return (s == null || s.trim().length() == 0);
    }

    public static String parseLogContent(List<LogEntity> list) {
        Gson gson = new Gson();
        List<String> contentList = new ArrayList<>();
        for (LogEntity entity : list) {
            if (!Utils.isTrimEmpty(entity.getContent())) {
                contentList.add(entity.getContent());
            }
        }
        return gson.toJson(contentList);
    }

    public static void logw(String msg) {
        if (Logdog.debug) {
            Log.w(TAG, msg);
        }
    }

    public static void loge(String msg) {
        if (Logdog.debug) {
            Log.e(TAG, msg);
        }
    }

    public static void logi(String msg) {
        if (Logdog.debug) {
            Log.i(TAG, msg);
        }
    }

}
