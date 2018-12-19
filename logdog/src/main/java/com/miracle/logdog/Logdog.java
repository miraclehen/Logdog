package com.miracle.logdog;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.miracle.logdog.db.LogDao;
import com.miracle.logdog.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * miracle
 * 2018/12/4 09:45
 */
public class Logdog {

    public static final String TAG = Logdog.class.getSimpleName();

    /** 上下文对象 */
    public static Context context;
    private static LogDao dao;
    private static ExecutorService executor;

    /** 是否启用调试 */
    public static boolean debug = false;

    /** 是否初始化了 */
    private static boolean initialized = false;

    /** 配置信息 */
    public static Config config;

    public static final int MODE_SAVE_THEN_PUSH = 1;
    public static final int MODE_PUSH_THEN_SAVE = 2;

    public static void init(Application context, String url) {
        Config config = new Config.Builder()
                .url(url)
                .build();
        Logdog.init(context, config);
    }

    public static void init(Application context, Config config) {
        if (config == null) {
            throw new LogdogException("config is null!");
        }
        Logdog.context = context;
        Logdog.config = config;

        dao = LogDao.getInstance();
        executor = Executors.newSingleThreadExecutor();
        initialized = true;
    }

    public static void enableDebug() {
        Logdog.debug = true;
    }

    public static void log(final Object src, int model) {
        if (src == null) {
            Log.w(TAG, "log src is null");
            return;
        }
        Gson gson = new Gson();
        Logdog.log(gson.toJson(src), model);
    }

    public static void log(final String json, int model) {
        if (Utils.isTrimEmpty(json)) {
            Log.w(TAG, "log json is empty or null");
            return;
        }
        if (model == MODE_SAVE_THEN_PUSH) {
            //先保存至本地然后满足条件的情况下推送到服务端
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    saveToDb(json);
                }
            });
        } else if (model == MODE_PUSH_THEN_SAVE) {
            //直接提交到服务端，如果失败，则写入数据库
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    LogEntity entity = new LogEntity();
                    entity.setContent(json);
                    boolean result = doPushWork(Arrays.asList(entity));
                    if (!result) {
                        saveToDb(json);
                        Utils.loge("提交数据数据到服务器失败！内容：" + json);
                    }
                }
            });
        }
    }

    private static void saveToDb(final String json) {
        long id = dao.insert(json);
        if (id == -1) {
            Utils.logi("add log faild! id is:" + id + " content is:" + json);
        } else {
            Utils.logi("add log success! id is:" + id + " content is:" + json);
        }
    }


    public static void pushMultiPart(final int size) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<LogEntity> list = dao.queryHead(size);
                if (list != null && !list.isEmpty()) {
                    boolean result = doPushWork(list);
                    doDeleteFromDB(list);
                }
            }
        });
    }

    public static void pushAll() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Config config = Logdog.config;
                List<LogEntity> list = dao.queryAll();
                boolean result = doPushWork(list);
                if (result) {
                    doDeleteFromDB(list);
                }
            }
        });
    }

    private static boolean doPushWork(List<LogEntity> list) {
        if (list != null && !list.isEmpty()) {
            String body = "";
            try {
                body = config.logPush.processBody(config.jsonBody, list);
            } catch (Exception e) {
                e.printStackTrace();
                Utils.loge("processBody data failed.");
                return false;
            }
            if (Utils.isTrimEmpty(body)) {
                return false;
            }
            boolean result = config.logPush.push(config.url, body, config.header);
            Utils.logi(result ? "成功推送" + list.size() + "条！" : "失败推送" + list.size() + "条!");
            return result;
        }
        return true;
    }

    private static boolean doDeleteFromDB(List<LogEntity> list) {
        List<Integer> idList = new ArrayList<>();
        for (LogEntity log : list) {
            idList.add(log.getId());
        }
        boolean result = LogDao.getInstance().removeHead(idList);
        if (result) {
            Utils.logi("remove " + list.size() + " log items after push server successfully!");
        }
        return result;
    }

    public static boolean isInitialized() {
        return initialized;
    }

}
