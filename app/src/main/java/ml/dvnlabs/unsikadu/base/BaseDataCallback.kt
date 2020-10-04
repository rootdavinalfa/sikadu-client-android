/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.base


interface BaseDataCallback<T> {
    fun onSuccess(data: T)

    fun onFailed(errorMessage: String?)

    fun onShowProgress()

    fun onHideProgress()
}