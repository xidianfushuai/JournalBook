package com.example.handsomefu.journalbook.db.dao;

import android.content.Context;

import com.example.handsomefu.journalbook.db.DatabaseHelper;
import com.example.handsomefu.journalbook.db.bean.Journal;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by HandsomeFu on 2016/11/1.
 */
public class JournalDao {
    private Dao<Journal, Integer> dao;
    private DatabaseHelper helper;
    public JournalDao(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(Journal.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void add(Journal journal) {
        try {
            dao.create(journal);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete(int id) {
        try {
            dao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, Journal journal) {
        try {
            Journal j = dao.queryForId(id);
            j.setContent(journal.getContent());
            j.setTitle(journal.getTitle());
            dao.update(j);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Journal> queryForAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
