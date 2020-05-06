/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.ui.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.base.BaseActivity
import ml.dvnlabs.unsikadu.base.BaseViewModel
import ml.dvnlabs.unsikadu.constant.constant
import ml.dvnlabs.unsikadu.model.StudentInfo
import ml.dvnlabs.unsikadu.ui.fragment.*
import ml.dvnlabs.unsikadu.util.database.CreateProfileDBHelper
import ml.dvnlabs.unsikadu.util.network.APINetworkRequest
import ml.dvnlabs.unsikadu.util.network.RequestQueueVolley
import ml.dvnlabs.unsikadu.util.network.listener.FetchDataListener
import ml.dvnlabs.unsikadu.viewmodel.StudentsViewModel
import org.json.JSONException
import org.json.JSONObject


class DashboardActivity : BaseActivity() {
    private lateinit var studenVM: StudentsViewModel
    private lateinit var baseVM: BaseViewModel

    var loading: AVLoadingIndicatorView? = null
    var dashContainer: RelativeLayout? = null
    var bottomNav: BottomNavigationView? = null
    var dbHelper: CreateProfileDBHelper? = null

    var studentInfo: StudentInfo? = null

    private var indexMenu = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBar(this, R.color.white, true)
        setContentView(R.layout.activity_dashboard)
        dbHelper = CreateProfileDBHelper(this)
        loading = findViewById(R.id.dashboardLoading)
        dashContainer = findViewById(R.id.mainDashboardContainer)
        bottomNav = findViewById(R.id.dashBottomNavi)
        if (!intent!!.extras!!.isEmpty) {
            val uid = intent.extras!!.getString("studentID")
            val pass = intent.extras!!.getString("password")
            val param = HashMap<String, String>()
            param["user"] = uid!!
            param["password"] = pass!!
            APINetworkRequest(
                this,
                loginAction,
                constant.loginUrl,
                APINetworkRequest.CODE_POST_REQUEST,
                param
            )
            bottomNavLogic()
        }
    }

    override fun onStop() {
        RequestQueueVolley.getInstance(this).stopAllRequest()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        bottomNav!!.selectedItemId = indexMenu
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        indexMenu = bottomNav!!.selectedItemId
    }

    private fun bottomNavLogic() {
        baseVM = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseVM.selectedItem.observe(this, Observer {
            bottomNav!!.selectedItemId = it
        })
        bottomNav!!.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.bottomMenuInfo -> {
                    setupFragment(
                        DashboardOverview(),
                        R.id.dashboardFrame,
                        DashboardOverview().tag.toString(), null
                    )
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottomMenuSchedule -> {
                    setupFragment(
                        DashboardSchedule(),
                        R.id.dashboardFrame,
                        DashboardSchedule().tag.toString(), null
                    )
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottomMenuGrade -> {
                    setupFragment(
                        DashboardGrade(),
                        R.id.dashboardFrame,
                        DashboardGrade().tag.toString(), null
                    )
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottomMenuFinance -> {
                    setupFragment(
                        DashboardFinance(),
                        R.id.dashboardFrame,
                        DashboardFinance().tag.toString(), null
                    )
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottomMenuSetting -> {
                    setupFragment(
                        DashboardSetting(),
                        R.id.dashboardFrame,
                        DashboardSetting().tag.toString(), null
                    )
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    fun readInfo(token: String) {
        val tokenPut: SharedPreferences.Editor =
            getSharedPreferences("session", Context.MODE_PRIVATE).edit()
        tokenPut.putString("token", token)
        tokenPut.apply()
        APINetworkRequest(
            this,
            readInfoListener,
            constant.infoUrl + token,
            APINetworkRequest.CODE_GET_REQUEST,
            null
        )
    }

    fun notAuthorizedAlert() {
        GlobalScope.launch {
            deleteProfile()
        }
        val errorDialog = AlertDialog.Builder(this@DashboardActivity)
        errorDialog.setTitle("Not Authorized")
            .setCancelable(false)
            .setMessage("Profile yang anda masukkan tidak memiliki akses\nPastikan user dan password yang dimasukkan benar!")
            .setPositiveButton("Kembali ke login") { _: DialogInterface?, _: Int ->
                super.onBackPressed()
            }
        val elErrorDialog = errorDialog.create()
        elErrorDialog.show()
    }

    private suspend fun deleteProfile() {
        withContext(Dispatchers.IO) {
            dbHelper!!.deleteAllProfile()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val pref: SharedPreferences =
            applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
        if (pref.contains("token")) {
            Log.e("Preferences: ", "Clearing...")
            pref.edit().clear().apply()
        }
    }

    override fun onBackPressed() {
        if (dashContainer!!.visibility == View.VISIBLE) {
            if (bottomNav!!.menu.getItem(0).isChecked) {
                val logoutDialog = AlertDialog.Builder(this@DashboardActivity)
                logoutDialog.setTitle("Logout dari profile ini?")
                    .setCancelable(false)
                    .setMessage("Aplikasi ini akan ditutup, apakah anda yakin?")
                    .setPositiveButton("Exit") { _: DialogInterface?, _: Int ->
                        val a = Intent(Intent.ACTION_MAIN)
                        a.addCategory(Intent.CATEGORY_HOME)
                        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(a)
                    }
                    .setNegativeButton("Cancel") { _: DialogInterface?, _: Int ->

                    }
                val elErrorDialog = logoutDialog.create()
                elErrorDialog.show()
            } else {
                bottomNav!!.menu.getItem(0).isChecked = true
                setupFragment(
                    DashboardOverview(),
                    R.id.dashboardFrame,
                    DashboardOverview().tag.toString(), null
                )
            }
        } else {
            super.onBackPressed()
        }
    }

    var loginAction: FetchDataListener = object : FetchDataListener {
        override fun onFetchFailure(msg: String?) {
            Log.e("ERROR: ", msg)
        }

        override fun onFetchStart() {
            loading!!.visibility = View.VISIBLE
            dashContainer!!.visibility = View.GONE
        }

        override fun onFetchComplete(data: String?) {
            try {
                val loginData = JSONObject(data)
                if (!loginData.getBoolean("Error")) {
                    readInfo(loginData.getString("Token"))
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }
    var readInfoListener: FetchDataListener = object : FetchDataListener {
        override fun onFetchComplete(data: String?) {
            try {
                val loginData = JSONObject(data)
                if (!loginData.getBoolean("Error")) {
                    loading!!.visibility = View.GONE
                    dashContainer!!.visibility = View.VISIBLE
                    //println(loginData.getJSONObject("Info").toString(1))
                    val info = loginData.getJSONObject("Info")
                    val studentID = info.getString("NPM")
                    val name = info.getString("Name")
                    val bornPlace = info.getString("PlaceBorn")
                    val bornDate = info.getString("BornOn")
                    val gender = info.getString("Gender")
                    val religion = info.getString("Religion")
                    val phone = info.getString("Phone")
                    val email = info.getString("Email")
                    val address = info.getString("Address")
                    val imgURL = info.getString("ProfilePict")
                    val collegeData = info.getJSONObject("College")
                    val faculty = collegeData.getString("Faculty")
                    val branch = collegeData.getString("Branch")
                    val degree = collegeData.getString("Degree")
                    val clas = collegeData.getString("Class")
                    val group = collegeData.getString("Group")
                    val status = collegeData.getString("Status")
                    studentInfo = StudentInfo(
                        studentID,
                        name,
                        bornPlace,
                        bornDate,
                        gender,
                        religion,
                        phone,
                        email,
                        address,
                        imgURL,
                        faculty,
                        branch,
                        degree,
                        clas,
                        group,
                        status
                    )
                    studenVM =
                        ViewModelProvider(this@DashboardActivity).get(StudentsViewModel::class.java)
                    studenVM.students = studentInfo

                    setupFragment(
                        DashboardOverview(),
                        R.id.dashboardFrame,
                        DashboardOverview().tag.toString()
                        , null
                    )
                    bottomNav!!.menu.getItem(0).isChecked = true
                    GlobalScope.launch {
                        updateProfileInfo(studentID, name, imgURL)
                    }
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        override fun onFetchFailure(msg: String?) {
            loading!!.visibility = View.GONE
            dashContainer!!.visibility = View.GONE
            notAuthorizedAlert()
        }

        override fun onFetchStart() {

        }
    }

    //Couroutine
    private suspend fun updateProfileInfo(studentID: String, name: String, imgURL: String) {
        withContext(Dispatchers.IO) {
            dbHelper!!.updateProfileInfo(
                studentID, name, imgURL
            )
            withContext(Dispatchers.Main) {
                Log.i("DB UPDATE -> ", "Profile up-to-date")
            }
        }
    }

}