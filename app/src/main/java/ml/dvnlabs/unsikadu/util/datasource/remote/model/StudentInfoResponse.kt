/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.util.datasource.remote.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudentInfoResponse(
    @SerializedName("Info")
    @Expose
    var info: Info
) : Parcelable

@Parcelize
data class Info(
    @SerializedName("NIK")
    @Expose
    var nik: String,
    @SerializedName("NPM")
    @Expose
    var npm: String,
    @SerializedName("Name")
    @Expose
    var name: String,
    @SerializedName("PlaceBorn")
    @Expose
    var placeBorn: String,
    @SerializedName("BornOn")
    @Expose
    var bornDate: String,
    @SerializedName("Gender")
    @Expose
    var gender: String,
    @SerializedName("Religion")
    @Expose
    var religion: String,
    @SerializedName("Phone")
    @Expose
    var phone: String,
    @SerializedName("Email")
    @Expose
    var email: String,
    @SerializedName("Address")
    @Expose
    var address: String,
    @SerializedName("ProfilePict")
    @Expose
    var pictureUrl: String,
    @SerializedName("College")
    @Expose
    var college: College
) : Parcelable

@Parcelize
data class College(
    @SerializedName("Faculty")
    @Expose
    var faculty: String,
    @SerializedName("Branch")
    @Expose
    var major: String,
    @SerializedName("Degree")
    @Expose
    var degree: String,
    @SerializedName("Class")
    @Expose
    var clas: String,
    @SerializedName("Group")
    @Expose
    var group: String,
    @SerializedName("Status")
    @Expose
    var status: String
) : Parcelable