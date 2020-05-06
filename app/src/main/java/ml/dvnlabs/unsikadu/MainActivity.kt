/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu

import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.frame_create_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ml.dvnlabs.unsikadu.base.BaseActivity
import ml.dvnlabs.unsikadu.ui.activity.DashboardActivity
import ml.dvnlabs.unsikadu.util.database.CreateProfileDBHelper
import ml.dvnlabs.unsikadu.util.database.model.ProfileList


class MainActivity : BaseActivity() {
    //EditText
    var inputUser : EditText? = null
    var inputPassword : EditText? = null

    //Button
    var profileCreateButton : MaterialButton? = null


    var dbHelper: CreateProfileDBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBar(this,R.color.white,true)
        dbHelper = CreateProfileDBHelper(this)
        if (dbHelper!!.hasProfile()){
            GlobalScope.launch {
                readSelectedProfile()
            }
        }else{
            setContentView(R.layout.activity_main)
            initialize()
            clickBehavior()
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        checkHasProfile()
    }

    private fun checkHasProfile() {
        if (dbHelper!!.hasProfile()) {
            GlobalScope.launch {
                readSelectedProfile()
            }
        }
    }

    private fun initialize() {
        inputUser = this.profileUserText
        inputPassword = this.profilePasswordText
        profileCreateButton = this.profileCreate
    }

    private fun clickBehavior() {
        profileCreateButton!!.setOnClickListener {
            val userid = inputUser!!.text!!.toString()
            val passwor = inputPassword!!.text!!.toString()
            if (userid != "" && passwor != "") {
                GlobalScope.launch {
                    addProfile(userid, passwor)
                }
            }
        }

    }

    private fun hasProfile(result: ProfileList) {
        val intent = Intent(
            this,
            DashboardActivity::class.java
        )
        intent.putExtra("id", result.ids)
        intent.putExtra("studentName", result.studentName)
        intent.putExtra("studentID", result.studentId)
        intent.putExtra("password", result.password)
        startActivity(intent)
    }

    private suspend fun addProfile(userID: String, password: String) {
        withContext(Dispatchers.IO) {
            dbHelper!!.createNewProfile(userID, password)

            withContext(Dispatchers.Main) {
                inputUser!!.text!!.clear()
                inputPassword!!.text!!.clear()
                checkHasProfile()
            }
        }
    }

    private suspend fun readSelectedProfile() {
        withContext(Dispatchers.IO) {
            val result = dbHelper!!.getSelectedProfile()!!

            withContext(Dispatchers.Main) {
                hasProfile(result)
            }
        }
    }
}
