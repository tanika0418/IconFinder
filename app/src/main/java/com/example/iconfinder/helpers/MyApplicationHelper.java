package com.example.iconfinder.helpers;

import android.app.Application;

import com.example.iconfinder.models.IconModel;
import com.example.iconfinder.models.IconSetModel;

import java.util.List;

public class MyApplicationHelper extends Application {
    IconSetModel iconSetModel;

    public MyApplicationHelper() {
    }

    public IconSetModel getIconSetModel() {
        return iconSetModel;
    }

    public void setIconSetModel(IconSetModel iconSetModel) {
        this.iconSetModel = iconSetModel;
    }
}
