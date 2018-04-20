package com.imdb.model;

/**
 * Created by prabhu on 29/3/18.
 */

/*
*
* This class has all details about Crew
*
* */
public class Crew {
    int id;
    String creditId, name, department, job, profilePath;
    String gender;

    public Crew(String creditId, int id, String name, String department, String job, String profilePath, String gender) {
        this.creditId = creditId;
        this.id = id;
        this.name = name;
        this.department = department;
        this.job = job;
        this.profilePath = profilePath;
        this.gender = gender;
    }

    public String getCreditId() {
        return creditId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getJob() {
        return job;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getGender() {
        return gender;
    }
}
