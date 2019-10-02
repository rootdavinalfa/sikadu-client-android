
/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.util.network.listener;

public interface FetchDataListener {
    void onFetchComplete(String data);

    void onFetchFailure(String msg);

    void onFetchStart();
}
