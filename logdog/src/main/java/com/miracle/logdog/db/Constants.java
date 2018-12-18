package com.miracle.logdog.db;

/**
 * miracle
 * 2018/12/4 15:53
 */
public class Constants {

    /** 表字段名称 */
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_CREATE_DATE_TIME = "create_datetime";

    /** 创建表 */
    public static final String CREATE_TABLE_SQL = "create table if not exists log(_id integer primary key autoincrement," +
            "content text, create_datetime varchar(30));";

    /** 表名称 */
    public static final String TABLE_NAME = "log";

    /** 数据库名称 */
    public static final String DB_NAME = "logdog_db";

    /** 数据库版本 */
    public static final int DB_VERSION = 1;
}
