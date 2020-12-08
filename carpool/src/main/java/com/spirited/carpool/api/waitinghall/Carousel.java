package com.spirited.carpool.api.waitinghall;


import java.io.Serializable;

public class Carousel implements Serializable {
    public String path, type;
    public String url;

    public Carousel(String path, String type, String url) {
        this.path = path;
        this.type = type;
        this.url = url;
    }
}
