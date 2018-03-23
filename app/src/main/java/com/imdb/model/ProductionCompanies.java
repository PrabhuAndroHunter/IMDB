package com.imdb.model;

/**
 * Created by prabhu on 20/3/18.
 */

public class ProductionCompanies {
    int id;
    String name;
    String logoPath;
    String origin_country;

    public ProductionCompanies(int id, String name, String logoPath, String origin_country) {
        this.id = id;
        this.name = name;
        this.logoPath = logoPath;
        this.origin_country = origin_country;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public String getOrigin_country() {
        return origin_country;
    }
}
