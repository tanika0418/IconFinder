package com.example.iconfinder.models;

import java.util.List;

public class IconSetModel {
    String name;
    int id;
    List<IconModel> icons;

    public IconSetModel() {
    }

    public IconSetModel(String name, int id, List<IconModel> icons) {
        this.name = name;
        this.id = id;
        this.icons = icons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<IconModel> getIcons() {
        return icons;
    }

    public void setIcons(List<IconModel> icons) {
        this.icons = icons;
    }
}
