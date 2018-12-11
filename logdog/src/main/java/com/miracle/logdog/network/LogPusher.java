package com.miracle.logdog.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.miracle.logdog.JsonMergeConflictException;
import com.miracle.logdog.LogEntity;
import com.miracle.logdog.Logdog;
import com.miracle.logdog.util.GsonTools;
import com.miracle.logdog.util.Utils;
import com.miracle.logdog.db.LogDao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * miracle
 * 2018/12/4 16:14
 */
public class LogPusher implements LogPush {

    private OkHttpClient client;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public LogPusher() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        if (Logdog.debug) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        client = builder.build();
    }

    @Override
    public String processBody(String jsonBody, List<LogEntity> list) throws Exception {
        Gson gson = new Gson();
        if (Utils.isTrimEmpty(jsonBody)) {
            return gson.toJson(list);
        }
        JsonParser parser = new JsonParser();
        JsonObject rightJsonObj = parser.parse(jsonBody).getAsJsonObject();

        List<JsonObject> jsonObjectList = new ArrayList<>();
        for (LogEntity log : list) {
            if (log == null || Utils.isTrimEmpty(log.getContent())) {
                continue;
            }
            try {
                JsonObject leftJsonObj = parser.parse(log.getContent()).getAsJsonObject();
                GsonTools.extendJsonObject(leftJsonObj, GsonTools.ConflictStrategy.PREFER_FIRST_OBJ, rightJsonObj);
                jsonObjectList.add(leftJsonObj);
            } catch (JsonMergeConflictException e) {
                e.printStackTrace();
            }
        }
        return gson.toJson(jsonObjectList);
    }

    @Override
    public boolean push(String url, String body, Map<String, String> extraHeaders) {
        RequestBody requestBody = RequestBody.create(JSON, body);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody);
        for (Map.Entry<String, String> header : extraHeaders.entrySet()) {
            if (!Utils.isTrimEmpty(header.getKey()) && !Utils.isTrimEmpty(header.getValue())) {
                builder.addHeader(header.getKey(), header.getValue());
            }
        }
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Utils.logi("push logs to server success!");
            } else {
                return false;
            }
        } catch (IOException e) {
            Log.w(Logdog.TAG, "push logs to server failure!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
