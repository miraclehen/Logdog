package com.miracle.logdog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;


/**
 * miracle
 * 2018/12/4 10:17
 */
public class LogDBHelper extends SQLiteOpenHelper {

    private static final String TAG = LogDBHelper.class.getSimpleName();
    private Context context;


    public LogDBHelper(Context context) {
        this(context, Constants.DB_NAME);
    }

    public LogDBHelper(Context context, String dbName) {
        super(context, dbName, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_TABLE_SQL);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
