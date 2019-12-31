package ru.job4j.retrofitexample.db.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ru.job4j.retrofitexample.db.dbhelper.DBClass;

public class Post implements Serializable, DBClass {
    @SerializedName("userId")
    private Integer user_id;
    @SerializedName("id")
    private Integer _id;
    private String title;

    @SerializedName("body")
    private String text;

    public Post() {
    }

    public Post(Integer id, Integer userId, String title, String text) {
        this(userId, title, text);
        this._id = id;
    }

    public Post(Integer userId, String title, String text) {
        this.user_id = userId;
        this.title = title;
        this.text = text;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Post{" +
                "user_id=" + user_id +
                ", _id=" + _id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
