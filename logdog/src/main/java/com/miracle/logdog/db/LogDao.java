package com.miracle.logdog.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.miracle.logdog.LogEntity;
import com.miracle.logdog.Logdog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * miracle
 * 2018/12/4 15:10
 */
public class LogDao {


    private DateFormat dateFormat;
    private SQLiteDatabase db;
    private static LogDao INSTANCE;

    public static LogDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LogDao();
        }
        return INSTANCE;
    }

    public LogDao(SQLiteDatabase db) {
        this.db = db;
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        }
    }

    public LogDao() {
        this(new LogDBHelper(Logdog.context).getWritableDatabase());
    }

    public long insert(String content) {
        ContentValues cv = new ContentValues();
        cv.put(Constants.COLUMN_CONTENT, content);
        cv.put(Constants.COLUMN_CREATE_DATE_TIME, dateFormat.format(new Date()));
        return db.insert(Constants.TABLE_NAME, null, cv);
    }

    public LogEntity queryById(long id) {
        Cursor cursor = db.query(Constants.TABLE_NAME, null, "_id = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToNext()) {
            return assembleLog(cursor);
        }
        return null;
    }

    public List<LogEntity> queryAll() {
        Cursor cursor = db.query(Constants.TABLE_NAME, null, null, null, null, null, null);
        List<LogEntity> logList = new ArrayList<>();
        while (cursor.moveToNext()) {
            LogEntity log = assembleLog(cursor);
            logList.add(log);
        }
        cursor.close();
        return logList;
    }

    public LogEntity query() {
        Cursor cursor = db.query(Constants.TABLE_NAME, null, null, null, null, null, null);
        LogEntity result = null;
        if (cursor.moveToNext()) {
            result = assembleLog(cursor);
        }
        cursor.close();
        return result;
    }

    public List<LogEntity> queryHead(int size) {
        Cursor cursor = db.query(Constants.TABLE_NAME, null, null,
                null, null, null, null, String.valueOf(size));
        List<LogEntity> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            LogEntity logEntity = assembleLog(cursor);
            result.add(logEntity);
        }
        cursor.close();
        return result;
    }

    public int removeAll() {
        return db.delete(Constants.TABLE_NAME, null, null);
    }

    public boolean removeHead(List<Integer> idList) {
        if (idList == null || idList.isEmpty()) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(Constants.TABLE_NAME);
        sb.append(" WHERE _id in");
        sb.append(" (");
        for (int i = 0; i < idList.size(); i++) {
            sb.append(idList.get(i));
            if (i != (idList.size() - 1)) {
                sb.append(",");
            }
        }
        sb.append(")");
        try {
            db.execSQL(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private LogEntity assembleLog(Cursor cursor) {

        int idIndex = cursor.getColumnIndex(Constants.COLUMN_ID);
        int contentIndex = cursor.getColumnIndex(Constants.COLUMN_CONTENT);
        int createDateIndex = cursor.getColumnIndex(Constants.COLUMN_CREATE_DATE_TIME);
        int id = cursor.getInt(idIndex);
        String content = cursor.getString(contentIndex);
        String createTime = cursor.getString(createDateIndex);

        LogEntity log = new LogEntity();
        log.setId(id);
        log.setContent(content);
        log.setCreateTime(createTime);

        return log;
    }

}
