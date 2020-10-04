/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.util.datasource

import ml.dvnlabs.unsikadu.util.datasource.remote.RemoteCallback
import ml.dvnlabs.unsikadu.util.datasource.remote.model.LoginResponse
import ml.dvnlabs.unsikadu.util.datasource.remote.model.StudentInfoResponse

interface GenericRemoteDataSource {
    fun loginUser(
        user: String,
        password: String,
        callback: RemoteCallback<LoginResponse>
    )

    fun getStudentInfo(
        token : String
        ,callback: RemoteCallback<StudentInfoResponse>)
}