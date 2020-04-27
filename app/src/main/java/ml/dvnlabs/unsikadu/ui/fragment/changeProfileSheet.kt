/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.ui.fragment

import android.content.DialogInterface
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.ui.view.list.ProfileListAdapter
import ml.dvnlabs.unsikadu.util.database.CreateProfileDBHelper
import ml.dvnlabs.unsikadu.util.database.model.ProfileList
import net.cachapa.expandablelayout.ExpandableLayout

class changeProfileSheet() : BottomSheetDialogFragment(){
    //Button
    var profileCreateButton : MaterialButton? = null

    //Input
    var inputUser : TextInputEditText? = null
    var inputPassword : TextInputEditText? = null

    //Layout
    var expandCreate : ExpandableLayout? = null
    var expandList : ExpandableLayout? = null
    var createButton : RelativeLayout?= null

    //Recycler
    var listProfile : RecyclerView? = null
    var adapter : ProfileListAdapter? = null

    //DB
    var dbHelper : CreateProfileDBHelper? = null

    var  onDismissListener : (String) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = CreateProfileDBHelper(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.sheet_change_profile, container, false).apply {
            expandCreate = findViewById(R.id.sheetExpandCreate)
            expandList = findViewById(R.id.sheetProfileListContainer)
            createButton = findViewById(R.id.profileSheetNewProfile)
            listProfile = findViewById(R.id.sheetProfileList)
            inputUser = findViewById(R.id.profileUserText)
            inputPassword = findViewById(R.id.profilePasswordText)
            profileCreateButton = findViewById(R.id.profileCreateButton)
        }
        bindProgressButton(profileCreateButton!!)
        clickBehavior()
        ReadProfileListAll().execute()
        return view
    }
    private fun clickBehavior(){
        createButton!!.setOnClickListener(View.OnClickListener {
            expandCreate!!.toggle()
            expandList!!.toggle()
        })
        profileCreateButton!!.setOnClickListener(View.OnClickListener {
            profileCreateButton!!.showProgress{
                buttonText = "Adding..."
                progressColor = Color.WHITE
            }
            val user = inputUser!!.text.toString()
            val pass = inputPassword!!.text.toString()
            if (user!="" && pass != ""){
                AddProfile().execute(user,pass)
            }
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener("")
    }


    inner class ReadProfileListAll : AsyncTask<String,Void,ArrayList<ProfileList>>(){
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): ArrayList<ProfileList> {
            return dbHelper!!.getProfileAll()!!
        }

        override fun onPostExecute(result: ArrayList<ProfileList>?) {
            super.onPostExecute(result)
            adapter = ProfileListAdapter(activity!!,result!!,R.layout.rv_profilelist)
            val layoutManager = LinearLayoutManager(context!!)
            listProfile!!.layoutManager = layoutManager
            listProfile!!.adapter = adapter
            adapter!!.onSelectProfileId = {id->
                ChangeSelectedProfile().execute(id.toString())
            }
            adapter!!.onDeleteProfileId={id->
                DeleteProfile().execute(id.toString())
            }
            if (adapter!!.itemCount == 0){
                onDismissListener("DELETE")
                dismiss()
            }
        }
    }

    inner class AddProfile: AsyncTask<String, Void, ArrayList<ProfileList>>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): ArrayList<ProfileList> {
            val userId : String = params[0]!!
            val password : String = params[1]!!
            dbHelper!!.createNewProfile(userId,password)
            return dbHelper!!.getProfileAll()!!
        }

        override fun onPostExecute(result: ArrayList<ProfileList>) {
            expandCreate!!.toggle()
            expandList!!.toggle()
            inputUser!!.text!!.clear()
            inputPassword!!.text!!.clear()
            profileCreateButton!!.hideProgress("Create profile")
            ReadProfileListAll().execute()
        }
    }

    inner class DeleteProfile: AsyncTask<String, Void, ArrayList<ProfileList>>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): ArrayList<ProfileList> {
            val idItem = params[0]!!.toInt()
            dbHelper!!.deleteProfile(idItem)
            return dbHelper!!.getProfileAll()!!
        }

        override fun onPostExecute(result: ArrayList<ProfileList>) {
            ReadProfileListAll().execute()
        }
    }

    inner class ChangeSelectedProfile: AsyncTask<String, Void, ArrayList<ProfileList>>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String?): ArrayList<ProfileList> {
            val idItem = params[0]!!.toInt()
            dbHelper!!.changeSelectedToProfile(idItem)
            return dbHelper!!.getProfileAll()!!
        }

        override fun onPostExecute(result: ArrayList<ProfileList>) {
            onDismissListener("SELECT")
            dismiss()
        }
    }
}