/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.ui.fragment

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.DiscretePathEffect
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ml.dvnlabs.unsikadu.MainActivity
import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.base.BaseViewModel
import ml.dvnlabs.unsikadu.ui.view.list.ProfileListAdapter
import ml.dvnlabs.unsikadu.util.database.CreateProfileDBHelper
import ml.dvnlabs.unsikadu.util.database.model.ProfileList
import ml.dvnlabs.unsikadu.viewmodel.StudentsViewModel
import net.cachapa.expandablelayout.ExpandableLayout

class ChangeProfileSheet : BottomSheetDialogFragment() {
    //Button
    var profileCreateButton: MaterialButton? = null

    //Input
    var inputUser: TextInputEditText? = null
    var inputPassword: TextInputEditText? = null

    //Layout
    var expandCreate: ExpandableLayout? = null
    var expandList: ExpandableLayout? = null
    var createButton: RelativeLayout? = null

    //Recycler
    var listProfile: RecyclerView? = null
    var adapter: ProfileListAdapter? = null

    //DB
    var dbHelper: CreateProfileDBHelper? = null

    var onDismissListener: (String) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = CreateProfileDBHelper(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.sheet_change_profile, container, false).apply {
            expandCreate = findViewById(R.id.sheetExpandCreate)
            expandList = findViewById(R.id.sheetProfileListContainer)
            createButton = findViewById(R.id.profileSheetNewProfile)
            listProfile = findViewById(R.id.sheetProfileList)
            inputUser = findViewById(R.id.profileUserText)
            inputPassword = findViewById(R.id.profilePasswordText)
            profileCreateButton = findViewById(R.id.profileCreate)
        }
        bindProgressButton(profileCreateButton!!)
        clickBehavior()
        GlobalScope.launch {
            readProfileListAll()
        }
        return view
    }

    private fun clickBehavior() {
        createButton!!.setOnClickListener {
            expandCreate!!.toggle()
            expandList!!.toggle()
        }
        profileCreateButton!!.setOnClickListener {
            profileCreateButton!!.showProgress {
                buttonText = "Adding..."
                progressColor = Color.WHITE
            }
            val user = inputUser!!.text.toString()
            val pass = inputPassword!!.text.toString()
            if (user != "" && pass != "") {
                GlobalScope.launch {
                    addProfile(user, pass)
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener("")
    }


    private suspend fun addProfile(user: String, password: String) {
        withContext(Dispatchers.IO) {
            dbHelper!!.createNewProfile(user, password)
            withContext(Dispatchers.Main) {
                expandCreate!!.toggle()
                expandList!!.toggle()
                inputUser!!.text!!.clear()
                inputPassword!!.text!!.clear()
                profileCreateButton!!.hideProgress("Create profile")
            }
            withContext(Dispatchers.Main) {
                dismiss()
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private suspend fun readProfileListAll() {
        withContext(Dispatchers.IO) {
            val listUser = dbHelper!!.getProfileAll()
            withContext(Dispatchers.Main) {
                adapter = ProfileListAdapter(requireActivity(), listUser!!, R.layout.rv_profilelist)
                val layoutManager = LinearLayoutManager(requireContext())
                listProfile!!.layoutManager = layoutManager
                listProfile!!.adapter = adapter
                adapter!!.onSelectProfileId = { id ->
                    GlobalScope.launch {
                        changeSelectedProfile(id)
                    }
                }
                adapter!!.onDeleteProfileId = { id ->
                    GlobalScope.launch {
                        deleteProfile(id)
                    }
                }
                if (adapter!!.itemCount == 0) {
                    onDismissListener("DELETE")
                    dismiss()
                }
            }
        }
    }

    private suspend fun deleteProfile(id: Int) {
        withContext(Dispatchers.IO) {
            dbHelper!!.deleteProfile(id)
            readProfileListAll()
        }
    }

    private suspend fun changeSelectedProfile(id: Int) {
        withContext(Dispatchers.IO) {
            dbHelper!!.changeSelectedToProfile(id)
            withContext(Dispatchers.Main) {
                dismiss()
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}