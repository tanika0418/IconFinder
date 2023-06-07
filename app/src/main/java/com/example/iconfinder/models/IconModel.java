package com.example.iconfinder.models;

public class IconModel {
    String name;
    int id;
    String previewUrl;
    String downloadUrl;
    boolean isPremium;

    public IconModel() {
    }

    public IconModel(String name, int id, String previewUrl, String downloadUrl, boolean isPremium) {
        this.name = name;
        this.id = id;
        this.previewUrl = previewUrl;
        this.downloadUrl = downloadUrl;
        this.isPremium = isPremium;
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

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
}
