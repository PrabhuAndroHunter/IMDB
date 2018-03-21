package com.imdb.network;

/**
 * Created by prabhu on 13/03/2018.
 */

public interface ResponseListener {
     void searchDone(Object object, String tag);
     void searchFail(String error, String tag);
}
