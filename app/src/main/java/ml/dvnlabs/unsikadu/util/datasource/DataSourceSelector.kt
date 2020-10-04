/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.util.datasource

import android.content.Context
import ml.dvnlabs.unsikadu.util.datasource.remote.RemoteCallback
import ml.dvnlabs.unsikadu.util.datasource.remote.RemoteDataSource
import ml.dvnlabs.unsikadu.util.datasource.remote.model.LoginResponse
import ml.dvnlabs.unsikadu.util.datasource.remote.model.StudentInfoResponse

class DataSourceSelector(private val context: Context) : GenericDataSource {

    private val remoteDataSource: RemoteDataSource = RemoteDataSource(context)


    override fun remoteLogin(
        user: String,
        password: String,
        callback: RemoteCallback<LoginResponse>
    ) {
        remoteDataSource.loginUser(user, password, callback)
    }

    override fun remoteGetStudentInfo(
        token: String,
        callback: RemoteCallback<StudentInfoResponse>
    ) {
        remoteDataSource.getStudentInfo(token, callback)
    }
}