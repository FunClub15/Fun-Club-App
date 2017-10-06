package com.techminds.funclub.entity;

/**
 * Created by mask on 8/11/17.
 */


public class Urls {
    String id;
    String url;
    String type;

    public Urls(String id, String url, String type) {
        this.id = id;
        this.url = url;
        this.type = type;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object urls) {
        if(this.url.equals(((Urls)urls).url) && this.type.equals(((Urls)urls).type))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Url : " + this.url + " Type : " + this.type;
    }
}
