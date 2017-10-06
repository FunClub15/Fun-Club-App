package com.techminds.funclub.entity;

/**
 * Created by mask on 8/27/17.
 */

public class ImageModel {

    String name, url;

    public ImageModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public ImageModel() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
