package com.imdb.network;

/**
 * Created by prabhu on 18/11/17.
 * This interface is used for check wifi state
 */

public interface NetworkStateListener {

    public void connected();
    public void notConnected();
}
