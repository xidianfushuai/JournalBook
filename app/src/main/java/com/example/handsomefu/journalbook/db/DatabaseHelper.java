package com.example.handsomefu.journalbook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.handsomefu.journalbook.db.bean.Journal;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HandsomeFu on 2016/11/1.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "journal_book.db";
    private static final int VERSION = 1;
    //每个表对应一个Dao
    private Map<String, Dao> daos = new HashMap<String, Dao>();
    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Journal.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource,Journal.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DatabaseHelper instance;
    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }
        return instance;
    }
    public synchronized Dao getDao(Class c) throws SQLException {
        Dao dao = null;
        String className = c.getSimpleName();
        if (daos.containsKey(className))
            dao = daos.get(className);
        if (dao == null) {
            dao = super.getDao(c);
            daos.put(className, dao);
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}
