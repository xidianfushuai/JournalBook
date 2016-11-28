package com.example.handsomefu.journalbook.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by HandsomeFu on 2016/11/1.
 */
@DatabaseTable(tableName = "tb_journal")
public class Journal {
    @DatabaseField(generatedId = true)//id为主键且自动生成
    private int id;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "content")
    private String content;
    @DatabaseField(columnName = "time")
    private String time;
    //canBeNull -表示不能为null；foreign=true表示是一个外键;第四句Journal对象则直接携带了user
//    @DatabaseField(canBeNull = true, foreign = true, columnName = "user_id", foreignAutoRefresh = true)
//    private User user;

//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public User getUser() {
//
//        return user;
//    }

    //必须提供一个空的构造方法，否则报错
    public Journal() {
        super();
    }

    public Journal(String title, String content, String time) {
        super();
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Journal [id=" + id + ", title=" + title + ", time=" + time + "]";
    }
}
