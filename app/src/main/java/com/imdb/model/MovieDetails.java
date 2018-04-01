package com.imdb.model;

import java.util.List;

/**
 * Created by prabhu on 20/3/18.
 */

public class MovieDetails {

    private String backdropPath;
    boolean adult;
    int budget;
    int revenue;
    String website;
    String language;
    String imdbId;
    String status;
    List <String> genresList;
    List <ProductionCompanies> productionCompaniesList;

    public MovieDetails(String backdropPath, boolean adult, int budget, int revenue, String website, String language, String imdbId, String status, List <String> genresList, List <ProductionCompanies> productionCompaniesList) {
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.budget = budget;
        this.revenue = revenue;
        this.website = website;
        this.language = language;
        this.imdbId = imdbId;
        this.status = status;
        this.genresList = genresList;
        this.productionCompaniesList = productionCompaniesList;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public int getBudget() {
        return budget;
    }

    public int getRevenue() {
        return revenue;
    }

    public String getWebsite() {
        return website;
    }

    public String getLanguage() {
        return language;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getStatus() {
        return status;
    }

    public List <String> getGenresList() {
        return genresList;
    }

    public List <ProductionCompanies> getProductionCompaniesList() {
        return productionCompaniesList;
    }
}
