package com.miracle.logdog.db;

import android.os.Build;

import com.google.gson.Gson;
import com.miracle.logdog.LogEntity;
import com.miracle.logdog.TestBean;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * miracle
 * 2018/12/4 17:58
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.LOLLIPOP)
public class LogDaoTest {

    LogDao dao;

    @Before
    public void setUp() throws Exception {
        LogDBHelper logDBHelper = new LogDBHelper(RuntimeEnvironment.application, new Random().nextInt(100) + ".db");
        dao = new LogDao(logDBHelper.getWritableDatabase());
    }

    @Test
    public void testInsertAndGet() throws Exception {
        TestBean bean = new TestBean();
        bean.setAddress("fuzhou");
        bean.setAge(18);
        bean.setName("xue");
        bean.setPhone("159802322222");

        Gson gson = new Gson();
        String json = gson.toJson(bean);
        dao.insert(json);
        List<LogEntity> logList = dao.queryAll();
        Assert.assertEquals(logList.size(), 1);
    }

    @Test
    public void testRemoveAll() throws Exception {
        TestBean bean1 = new TestBean();
        bean1.setAddress("fuzhou");
        bean1.setAge(18);
        bean1.setName("xue");
        bean1.setPhone("159802322222");

        TestBean bean2 = new TestBean();
        bean2.setAddress("hangzhou");
        bean2.setAge(24);
        bean2.setName("han");
        bean2.setPhone("15699998888");

        Gson gson = new Gson();

        dao.insert(gson.toJson(bean1));
        dao.insert(gson.toJson(bean2));

        Assert.assertEquals(dao.queryAll().size(), 2);
        int count = dao.removeAll();
        Assert.assertEquals(count, 2);
        Assert.assertEquals(dao.queryAll().size(), 0);
    }


    @Test
    public void testIsPushed() throws Exception {
        TestBean bean2 = new TestBean();
        bean2.setAddress("hangzhou");
        bean2.setAge(24);
        bean2.setName("han");
        bean2.setPhone("15699998888");

        Gson gson = new Gson();
        String json = gson.toJson(bean2);
        long id = dao.insert(json);

        Assert.assertNotEquals(id,-1);

        LogEntity logEntity = dao.queryById(id);
        Assert.assertNotNull(logEntity);
        Assert.assertEquals(logEntity.isPushed(),false);

    }

}