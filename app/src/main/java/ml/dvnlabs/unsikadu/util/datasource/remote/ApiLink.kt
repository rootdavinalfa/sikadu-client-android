/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.util.datasource.remote

import ml.dvnlabs.unsikadu.constant.constant
import ml.dvnlabs.unsikadu.util.datasource.remote.model.LoginResponse
import ml.dvnlabs.unsikadu.util.datasource.remote.model.StudentInfoResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiLink {
    @FormUrlEncoded
    @POST(constant.loginUrl)
    fun loginUser(
        @Field("user") user: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("${constant.infoUrl}{token}")
    fun getInfoStudent(@Path("token") token: String): Call<StudentInfoResponse>
}