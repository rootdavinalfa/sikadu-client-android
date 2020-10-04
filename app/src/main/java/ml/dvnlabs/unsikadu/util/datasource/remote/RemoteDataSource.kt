/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.util.datasource.remote

import android.content.Context
import ml.dvnlabs.unsikadu.util.datasource.GenericRemoteDataSource
import ml.dvnlabs.unsikadu.util.datasource.remote.model.LoginResponse
import ml.dvnlabs.unsikadu.util.datasource.remote.model.StudentInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource(private val context: Context) : GenericRemoteDataSource {

    override fun loginUser(
        user: String,
        password: String,
        callback: RemoteCallback<LoginResponse>
    ) {
        callback.onShowProgress()
        val call = ApiService.getApiClient(context).create(ApiLink::class.java)
            .loginUser(user, password)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>?,
                response: Response<LoginResponse>?
            ) {
                response?.body()?.let { callback.onSuccess(it) }
            }

            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                callback.onHideProgress()
                callback.onFailed(t?.localizedMessage)
            }
        })
    }

    override fun getStudentInfo(token: String, callback: RemoteCallback<StudentInfoResponse>) {
        callback.onShowProgress()
        val call = ApiService.getApiClient(context).create(ApiLink::class.java)
            .getInfoStudent(token)
        call.enqueue(object : Callback<StudentInfoResponse> {
            override fun onResponse(
                call: Call<StudentInfoResponse>?,
                response: Response<StudentInfoResponse>?
            ) {
                callback.onHideProgress()
                response?.body()?.let { callback.onSuccess(it) }
            }

            override fun onFailure(call: Call<StudentInfoResponse>?, t: Throwable?) {
                callback.onHideProgress()
                callback.onFailed(t?.localizedMessage)
            }
        })
    }
}