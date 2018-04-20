package com.imdb.model;

/**
 * Created by prabhu on 20/3/18.
 */

/*
*
* This class has all details about Cast
*
* */
public class Cast {
    int castId, id;
    String character, name, profilePath;
    String gender;

    public Cast(int castId, int id, String character, String name, String profilePath, String gender) {
        this.castId = castId;
        this.id = id;
        this.character = character;
        this.name = name;
        this.profilePath = profilePath;
        this.gender = gender;
    }

    public int getCastId() {
        return castId;
    }

    public int getId() {
        return id;
    }

    public String getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getGender() {
        return gender;
    }
}
