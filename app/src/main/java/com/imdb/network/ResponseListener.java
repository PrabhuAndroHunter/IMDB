package com.imdb.network;

/**
 * Created by prabhu on 13/03/2018.
 */

/*
*
* This Interface is used to get information on network process.
*
* */
public interface ResponseListener {
    /* This method gets called when the requested data parsed successfully*/
    void searchDone(Object object, String tag);

    /* This method gets called when there is an error in network call*/
    void searchFail(String error, String tag);
}
