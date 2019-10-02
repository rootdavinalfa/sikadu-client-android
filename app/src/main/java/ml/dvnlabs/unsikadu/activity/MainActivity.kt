/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.activity

import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.wang.avi.AVLoadingIndicatorView
import ml.dvnlabs.unsikadu.fragment.changeProfileSheet
import ml.dvnlabs.unsikadu.util.database.CreateProfileDBHelper
import ml.dvnlabs.unsikadu.util.database.model.ProfileList
import ml.dvnlabs.unsikadu.R


class MainActivity : AppCompatActivity(){
    //Textview
    var header : TextView? = null
    var nameProfile :TextView? = null
    var idProfile : TextView? = null
    var changeProfile : TextView? = null
    var privacyProfile : TextView? = null
    //Imageview
    var profileBack : ImageView? = null
    var profileImage : ImageView? = null

    //Button
    var profileCreateButton : MaterialButton? = null

    //Input
    var inputUser : TextInputEditText? = null
    var inputPassword : TextInputEditText? = null

    //Layout
    var createContainer : RelativeLayout? = null
    var selectContainer : LinearLayout? = null
    var profileSelect : RelativeLayout? = null
    var profileCreatorContainer : FrameLayout? = null

    var dbHelper : CreateProfileDBHelper? = null

    private val changeProfSheet = changeProfileSheet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
        dbHelper = CreateProfileDBHelper(this)

        clickBehavior()

    }

    override fun onPostResume() {
        super.onPostResume()
        checkHasProfile()
    }
    private fun checkHasProfile(){
        if (dbHelper!!.HasProfile()){
            header!!.text = "Welcome"
            profileBack!!.visibility = View.GONE
            createContainer!!.visibility = View.GONE
            selectContainer!!.visibility = View.VISIBLE
            ReadSelectedProfile().execute()
        }else{
            header!!.text = "New?!"
            profileBack!!.visibility = View.GONE
            createContainer!!.visibility = View.VISIBLE
            selectContainer!!.visibility = View.GONE
        }
    }
    private fun initialize(){
        header = findViewById(R.id.profileHeader)
        nameProfile = findViewById(R.id.profileName)
        idProfile = findViewById(R.id.profileId)
        changeProfile = findViewById(R.id.profileChangeButton)
        profileBack = findViewById(R.id.profileBack)
        profileImage = findViewById(R.id.profileImage)
        createContainer = findViewById(R.id.profileCreateContainer)
        selectContainer = findViewById(R.id.profileSelectContainer)
        profileSelect = findViewById(R.id.profileSelectCurrent)
        privacyProfile = findViewById(R.id.profilePrivacy)
        profileCreatorContainer = findViewById(R.id.profileCreator)
        profileCreateButton = findViewById(R.id.profileCreateButton)
        inputPassword = findViewById(R.id.profilePasswordText)
        inputUser = findViewById(R.id.profileUserText)
    }
    private fun clickBehavior(){
        createContainer!!.setOnClickListener (View.OnClickListener {
            profileCreatorContainer!!.visibility = View.VISIBLE
            createContainer!!.visibility = View.GONE
            profileBack!!.visibility = View.VISIBLE
            header!!.text = "Create Profile"
        }
        )
        profileBack!!.setOnClickListener {
            profileCreatorContainer!!.visibility = View.GONE
            checkHasProfile()
        }
        profileCreateButton!!.setOnClickListener {
            val userid = inputUser!!.text!!.toString()
            val passwor = inputPassword!!.text!!.toString()
            if (userid!="" && passwor != ""){
                AddProfile().execute(userid,passwor)
            }
        }
        changeProfile!!.setOnClickListener {
            changeProfSheet.show(supportFragmentManager,changeProfSheet.tag)
            changeProfSheet.onDismissListener = {
                checkHasProfile()
            }
        }
        privacyProfile!!.setOnClickListener {
            tosPopup()
        }

    }

    private fun tosPopup() {
        val loading: AVLoadingIndicatorView
        val close: ImageView
        val header: TextView
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.run { inflate(R.layout.frame_webview, null) }
        val webView: WebView =
            dialogView.findViewById(R.id.animize_webview)
        webView.loadUrl("http://dvnlabs.ml/unsikadu/privacytos.html")
        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        loading = dialogView.findViewById(R.id.webview_loading)
        header = dialogView.findViewById(R.id.webview_title_head)
        header.text = "Term Of Service & Privacy"
        close = dialogView.findViewById(R.id.informa_close)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                loading.show()
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loading.hide()
                Log.e("URL FINISH:", url)
            }
        }
        dialogBuilder.setView(dialogView)
        val markerPopUpDialog: Dialog = dialogBuilder.create()
        markerPopUpDialog.show()
        close.setOnClickListener { markerPopUpDialog.dismiss() }
    }

    private fun hasProfile(result: ProfileList?){
        nameProfile!!.text = result!!.studentName
        idProfile!!.text = result.studentId
        Glide.with(this).applyDefaultRequestOptions(
            RequestOptions()
            .placeholder(R.drawable.ic_mortarboard)
            .error(R.drawable.ic_mortarboard))
            .load(result.profileImgUrl)
            .transition(DrawableTransitionOptions().crossFade())
            .apply(bitmapTransform(CircleCrop())
                .diskCacheStrategy(DiskCacheStrategy.ALL).override(300,300))
            .into(profileImage!!)
        profileSelect!!.setOnClickListener {
            val intent  = Intent(this,DashboardActivity::class.java)
            intent.putExtra("id",result.ids)
            intent.putExtra("studentName",result.studentName)
            intent.putExtra("studentID",result.studentId)
            intent.putExtra("password",result.password)
            startActivity(intent)
        }
    }


    inner class AddProfile:AsyncTask<String,Void,ProfileList>(){
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): ProfileList {
            val userId : String = params[0]!!
            val password : String = params[1]!!
            dbHelper!!.createNewProfile(userId,password)
            return dbHelper!!.getSelectedProfile()!!
        }

        override fun onPostExecute(result: ProfileList?) {
            inputUser!!.text!!.clear()
            inputPassword!!.text!!.clear()
            checkHasProfile()
            profileCreatorContainer!!.visibility = View.GONE
            hasProfile(result)
        }
    }

    inner class ReadSelectedProfile:AsyncTask<String,Void,ProfileList>(){
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): ProfileList {
            return dbHelper!!.getSelectedProfile()!!
        }

        override fun onPostExecute(result: ProfileList?) {
            profileCreatorContainer!!.visibility = View.GONE
            hasProfile(result)
        }
    }
}
