package com.miracle.logdog.network;

import android.util.Log;

import com.miracle.logdog.LogEntity;

import java.util.List;
import java.util.Map;

/**
 * miracle
 * 2018/12/4 16:39
 */
public interface LogPush {

    String processBody(String jsonBody, List<LogEntity> list) throws Exception;

    boolean push(String url, String body, Map<String, String> extraHeaders);
}
