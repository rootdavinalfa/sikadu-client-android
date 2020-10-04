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
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ml.dvnlabs.unsikadu.MainActivity
import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.base.BaseActivity
import ml.dvnlabs.unsikadu.base.BaseViewModel
import ml.dvnlabs.unsikadu.model.StudentInfo
import ml.dvnlabs.unsikadu.ui.fragment.*
import ml.dvnlabs.unsikadu.util.database.CreateProfileDBHelper
import ml.dvnlabs.unsikadu.util.datasource.DataSourceSelector
import ml.dvnlabs.unsikadu.util.datasource.remote.RemoteCallback
import ml.dvnlabs.unsikadu.util.datasource.remote.model.LoginResponse
import ml.dvnlabs.unsikadu.util.datasource.remote.model.StudentInfoResponse
import ml.dvnlabs.unsikadu.util.network.RequestQueueVolley
import ml.dvnlabs.unsikadu.viewmodel.StudentsViewModel


class DashboardActivity : BaseActivity() {
    private lateinit var studenVM: StudentsViewModel
    private lateinit var baseVM: BaseViewModel
    private lateinit var dataSourceSelector: DataSourceSelector

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
        dataSourceSelector = DataSourceSelector(this)
        dbHelper = CreateProfileDBHelper(this)
        loading = findViewById(R.id.dashboardLoading)
        dashContainer = findViewById(R.id.mainDashboardContainer)
        bottomNav = findViewById(R.id.dashBottomNavi)
        if (!intent!!.extras!!.isEmpty) {
            val uid = intent.extras!!.getString("studentID")
            val pass = intent.extras!!.getString("password")
            if (!uid.isNullOrEmpty() && !pass.isNullOrEmpty()) {
                login(uid, pass)
            }
            bottomNavLogic()
        }
    }

    private fun login(uid: String, pass: String) {
        dataSourceSelector.remoteLogin(
            uid, pass, object : RemoteCallback<LoginResponse> {
                override fun onSuccess(data: LoginResponse) {
                    readInfo(data.token)
                }

                override fun onFailed(errorMessage: String?) {
                    Log.e("ERROR: ", errorMessage)
                }

                override fun onShowProgress() {
                    loading!!.visibility = View.VISIBLE
                    dashContainer!!.visibility = View.GONE
                }

                override fun onHideProgress() {
                }
            }
        )

    }

    override fun onStop() {
        RequestQueueVolley.getInstance(this)!!.stopAllRequest()
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
        dataSourceSelector.remoteGetStudentInfo(token,
            object : RemoteCallback<StudentInfoResponse> {
                override fun onSuccess(data: StudentInfoResponse) {
                    loading!!.visibility = View.GONE
                    dashContainer!!.visibility = View.VISIBLE
                    val info = data.info
                    val studentID = info.npm
                    val name = info.name
                    val bornPlace = info.placeBorn
                    val bornDate = info.bornDate
                    val gender = info.gender
                    val religion = info.religion
                    val phone = info.phone
                    val email = info.email
                    val address = info.address
                    val imgURL = info.pictureUrl
                    val faculty = info.college.faculty
                    val branch = info.college.major
                    val degree = info.college.degree
                    val clas = info.college.clas
                    val group = info.college.group
                    val status = info.college.status
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
                        DashboardOverview().tag.toString(), null
                    )
                    bottomNav!!.menu.getItem(0).isChecked = true
                    lifecycleScope.launch {
                        updateProfileInfo(studentID, name, imgURL)
                    }
                }

                override fun onFailed(errorMessage: String?) {
                    loading!!.visibility = View.GONE
                    dashContainer!!.visibility = View.GONE
                    notAuthorizedAlert()
                }

                override fun onShowProgress() {
                }

                override fun onHideProgress() {
                    loading!!.visibility = View.GONE
                }
            })
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
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
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