package ru.job4j.retrofitexample.db.models;

import com.google.gson.annotations.SerializedName;

import ru.job4j.retrofitexample.db.dbhelper.DBClass;

public class Comment implements DBClass {
    @SerializedName("postId")
    private Integer post_id;
    @SerializedName("id")
    private Integer _id;
    private String name;
    private String email;
    @SerializedName("body")
    private String text;

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
