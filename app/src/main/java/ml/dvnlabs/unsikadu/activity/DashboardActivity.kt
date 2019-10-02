/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wang.avi.AVLoadingIndicatorView
import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.constant.constant
import ml.dvnlabs.unsikadu.fragment.*
import ml.dvnlabs.unsikadu.model.StudentInfo
import ml.dvnlabs.unsikadu.util.database.CreateProfileDBHelper
import ml.dvnlabs.unsikadu.util.database.model.ProfileList
import ml.dvnlabs.unsikadu.util.network.APINetworkRequest
import ml.dvnlabs.unsikadu.util.network.listener.FetchDataListener
import org.json.JSONException
import org.json.JSONObject


class DashboardActivity : AppCompatActivity(){
    var loading : AVLoadingIndicatorView? = null
    var dashContainer : RelativeLayout? = null
    var bottomNav : BottomNavigationView? = null
    var dbHelper : CreateProfileDBHelper? = null

    var studentInfo : StudentInfo? = null

    private var indexMenu = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        dbHelper = CreateProfileDBHelper(this)

        loading = findViewById(R.id.dashboardLoading)
        dashContainer = findViewById(R.id.mainDashboardContainer)
        bottomNav = findViewById(R.id.dashBottomNavi)

        if (!intent!!.extras!!.isEmpty){
            val uid = intent.extras!!.getString("studentID")
            val pass = intent.extras!!.getString("password")
            val param = HashMap<String,String>()
            param["user"] = uid!!
            param["password"] = pass!!
            APINetworkRequest(this,loginAction,constant.loginUrl,APINetworkRequest.CODE_POST_REQUEST,param)
            bottomNavLogic()
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNav!!.selectedItemId = indexMenu
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        indexMenu = bottomNav!!.selectedItemId
    }

    private fun bottomNavLogic() {
        bottomNav!!.setOnNavigationItemSelectedListener { item: MenuItem ->
            when(item.itemId){
                R.id.bottomMenuInfo->{
                    closeFinanceFragment()
                    closeEngineerFragment()
                    closeScheduleFragment()
                    closeGradeFragment()
                    openInfoFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottomMenuSchedule->{
                    closeFinanceFragment()
                    closeEngineerFragment()
                    closeInfoFragment()
                    closeGradeFragment()
                    openScheduleFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottomMenuGrade->{
                    closeFinanceFragment()
                    closeEngineerFragment()
                    closeInfoFragment()
                    closeScheduleFragment()
                    openGradeFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottomMenuFinance->{
                    closeEngineerFragment()
                    closeInfoFragment()
                    closeScheduleFragment()
                    closeGradeFragment()
                    openFinanceFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottomMenuEngineer->{
                    closeGradeFragment()
                    closeFinanceFragment()
                    closeInfoFragment()
                    closeScheduleFragment()
                    openEngineerFragment()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    private fun openInfoFragment(){
        val dashboardInfo = dashboardInfo()
        global.addFragment(supportFragmentManager,dashboardInfo,R.id.frameFragmentInfo,"FRAGMENT_HOME","NULL")
    }
    private fun closeInfoFragment(){
        val fragmentManager : FragmentManager = supportFragmentManager
        val simpleFragment: dashboardInfo? = fragmentManager
            .findFragmentById(R.id.frameFragmentInfo) as dashboardInfo?
        if (simpleFragment != null){
            fragmentManager.beginTransaction().remove(simpleFragment).commit()
        }
    }
    private fun openScheduleFragment(){
        val dashboardSchedule = dashboardSchedule()
        global.addFragment(supportFragmentManager,dashboardSchedule,R.id.frameFragmentSchedule,"FRAGMENT_OTHER","NULL")
    }
    private fun closeScheduleFragment(){
        val fragmentManager : FragmentManager = supportFragmentManager
        val simpleFragment: dashboardSchedule? = fragmentManager
            .findFragmentById(R.id.frameFragmentSchedule) as dashboardSchedule?
        if (simpleFragment != null){
            fragmentManager.beginTransaction().remove(simpleFragment).commit()
        }
    }

    private fun openFinanceFragment(){
        val dashboardFinance = dashboardFinance()
        global.addFragment(supportFragmentManager,dashboardFinance,R.id.frameFragmentFinance,"FRAGMENT_OTHER","NULL")
    }
    private fun closeFinanceFragment(){
        val fragmentManager : FragmentManager = supportFragmentManager
        val simpleFragment: dashboardFinance? = fragmentManager
            .findFragmentById(R.id.frameFragmentFinance) as dashboardFinance?
        if (simpleFragment != null){
            fragmentManager.beginTransaction().remove(simpleFragment).commit()
        }
    }

    private fun openEngineerFragment(){
        val dashboardEngineer = dashboardEngineer()
        global.addFragment(supportFragmentManager,dashboardEngineer,R.id.frameFragmentEngineer,"FRAGMENT_OTHER","NULL")
    }
    private fun closeEngineerFragment(){
        val fragmentManager : FragmentManager = supportFragmentManager
        val simpleFragment: dashboardEngineer? = fragmentManager
            .findFragmentById(R.id.frameFragmentEngineer) as dashboardEngineer?
        if (simpleFragment != null){
            fragmentManager.beginTransaction().remove(simpleFragment).commit()
        }
    }

    private fun openGradeFragment(){
        val dashboardGrade = dashboardGrade()
        global.addFragment(supportFragmentManager,dashboardGrade,R.id.frameFragmentGrade,"FRAGMENT_OTHER","NULL")
    }
    private fun closeGradeFragment(){
        val fragmentManager : FragmentManager = supportFragmentManager
        val simpleFragment: dashboardGrade? = fragmentManager
            .findFragmentById(R.id.frameFragmentGrade) as dashboardGrade?
        if (simpleFragment != null){
            fragmentManager.beginTransaction().remove(simpleFragment).commit()
        }
    }

    fun readInfo(token : String){
        val tokenPut : SharedPreferences.Editor = getSharedPreferences("session", Context.MODE_PRIVATE).edit()
        tokenPut.putString("token",token)
        tokenPut.apply()
        APINetworkRequest(this,readInfoListener,constant.infoUrl+token,APINetworkRequest.CODE_GET_REQUEST,null)
    }

    fun notAuthorizedAlert(){
        val errorDialog = AlertDialog.Builder(this@DashboardActivity)
        errorDialog.setTitle("Not Authorized")
            .setCancelable(false)
            .setMessage("Profile yang anda pilih tidak memiliki akses\nSilahkan pilih profil lain/ buat baru dan pastikan user dan password yang dimasukkan benar!")
            .setPositiveButton("Pilih profil lain") { _: DialogInterface?, _: Int ->
                onBackPressed()
            }
        val elErrorDialog = errorDialog.create()
        elErrorDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        val pref : SharedPreferences = applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
        if (pref.contains("token")){
            Log.e("Preferences: ","Clearing...")
            pref.edit().clear().apply()
        }
    }

    override fun onBackPressed() {
        if(dashContainer!!.visibility == View.VISIBLE){

            if(bottomNav!!.menu.getItem(0).isChecked ){
                val logoutDialog = AlertDialog.Builder(this@DashboardActivity)
                logoutDialog.setTitle("Logout dari profile ini?")
                    .setCancelable(false)
                    .setMessage("Profile yang saat ini dipakai akan di logout apakah anda yakin?")
                    .setPositiveButton("Logout") { _: DialogInterface?, _: Int ->
                        val intent = Intent(this,MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { _: DialogInterface?, _:Int ->

                    }
                val elErrorDialog = logoutDialog.create()
                elErrorDialog.show()
            }else{
                bottomNav!!.menu.getItem(0).isChecked = true
                closeGradeFragment()
                closeScheduleFragment()
                openInfoFragment()
            }
        }else{
            super.onBackPressed()
        }
    }

    var loginAction : FetchDataListener = object : FetchDataListener{
        override fun onFetchFailure(msg: String?) {
            Log.e("ERROR: ",msg)
        }

        override fun onFetchStart() {
            loading!!.visibility = View.VISIBLE
            dashContainer!!.visibility = View.GONE
        }

        override fun onFetchComplete(data: String?) {
            try {
                val loginData = JSONObject(data)
                if (!loginData.getBoolean("Error")){
                    readInfo(loginData.getString("Token"))
                }

            }catch (e : JSONException){
                e.printStackTrace()
            }
        }
    }
    var readInfoListener : FetchDataListener = object : FetchDataListener{
        override fun onFetchComplete(data: String?) {
            try {
                val loginData = JSONObject(data)
                if (!loginData.getBoolean("Error")){
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
                    studentInfo = StudentInfo(studentID,name,bornPlace,bornDate,gender,religion,phone,email,address,imgURL,faculty,branch,degree,clas,group,status)

                    openInfoFragment()
                    bottomNav!!.menu.getItem(0).isChecked = true
                    updateProfileInfo().execute(studentID,name,imgURL)
                }

            }catch (e : JSONException){
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
     inner class updateProfileInfo : AsyncTask<String,Void,ProfileList>(){
        override fun onPostExecute(result: ProfileList?) {
            super.onPostExecute(result)
        }

        //Param 0 = student Id , param 1 = name , param 2 = imgurl
        override fun doInBackground(vararg params: String?): ProfileList {
            dbHelper!!.updateProfileInfo(params[0].toString(),params[1].toString(),params[2].toString())
            return dbHelper!!.getSelectedProfile()!!
        }

        override fun onPreExecute() {
            super.onPreExecute()
            Log.e("DBHELPER -> ","Profile up-to-date")
        }
    }
}