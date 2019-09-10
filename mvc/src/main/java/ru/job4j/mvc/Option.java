package ru.job4j.mvc;

import java.io.Serializable;

public class Option implements Serializable {
    private int id;
    private String text;

    public Option() {
    }

    public Option(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
