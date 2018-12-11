package com.miracle.logdog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.miracle.logdog.util.Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * miracle
 * 2018/12/4 17:00
 */
public class UtilsTest {

    @Test
    public void testParseLogEntity() throws Exception {

    }

    @Test
    public void testGson() throws Exception {
        TestBean bean1 = new TestBean();
        bean1.setAddress("fuzhou");
        bean1.setAge(18);
        bean1.setPhone("159802322222");

        Gson gson = new Gson();
        String jsonArray = "[\"Android\",\"Java\",\"PHP\"]";
        String[] strings = gson.fromJson(jsonArray, String[].class);
        System.out.println(strings.length);

        List<String> list = gson.fromJson(jsonArray, new TypeToken<List<String>>(){}.getType());
        System.out.println(list.size());

        Gson gson1 = new GsonBuilder()
                .serializeNulls()
                .create();
        gson1.toJson(bean1, System.out);
    }

    @Test
    public void testParseLogContent()throws Exception{
        LogEntity logEntity = new LogEntity();
        logEntity.setPushed(false);
        logEntity.setCreateTime("1993-08-28");
        logEntity.setContent("java");

        LogEntity logEntity2 = new LogEntity();
        logEntity2.setPushed(false);
        logEntity2.setCreateTime("1993-08-28");

        LogEntity logEntity3 = new LogEntity();
        logEntity3.setPushed(false);
        logEntity3.setCreateTime("1995-12-21");
        logEntity3.setContent("php");

        List<LogEntity> list = new ArrayList<>();
        list.add(logEntity);
        list.add(logEntity2);
        list.add(logEntity3);

        String json = Utils.parseLogContent(list);
        System.out.println(json);
    }

}