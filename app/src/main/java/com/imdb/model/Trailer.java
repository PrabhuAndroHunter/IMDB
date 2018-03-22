package com.imdb.model;

/**
 * Created by prabhu on 20/3/18.
 */

public class Trailer {
    private String name;
    private String vedioLink;

    public Trailer(String name, String vedioLink) {
        this.name = name;
        this.vedioLink = vedioLink;
    }

    public String getName() {
        return name;
    }

    public String getVedioLink() {
        return vedioLink;
    }
}
