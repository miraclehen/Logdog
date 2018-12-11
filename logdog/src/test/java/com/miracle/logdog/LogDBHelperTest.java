package com.miracle.logdog;

import android.os.Build;

import com.google.gson.Gson;
import com.miracle.logdog.db.LogDBHelper;
import com.miracle.logdog.db.LogDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

/**
 * miracle
 * 2018/12/4 15:06
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.LOLLIPOP)
public class LogDBHelperTest {


    LogDao dao;

    @Before
    public void setUp() throws Exception {
        //随机数做数据库名称，让每个测试方法，都用不同数据库，保证数据唯一性
        LogDBHelper dbHelper = new LogDBHelper(RuntimeEnvironment.application, new Random().nextInt(1000) + ".db");
        dao = new LogDao(dbHelper.getWritableDatabase());
    }



    @Test
    public void testDateTime2() throws Exception {
        SimpleDateFormat   dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        System.out.print(dateFormat.format(new Date()));
    }




}
