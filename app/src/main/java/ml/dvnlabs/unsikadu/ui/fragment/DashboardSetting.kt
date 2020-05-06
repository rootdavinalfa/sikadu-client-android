/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.frame_setting.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ml.dvnlabs.unsikadu.MainActivity

import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.base.BaseViewModel
import ml.dvnlabs.unsikadu.model.StudentInfo
import ml.dvnlabs.unsikadu.util.database.CreateProfileDBHelper
import ml.dvnlabs.unsikadu.viewmodel.StudentsViewModel

class DashboardSetting : Fragment() {
    private lateinit var viewModel: StudentsViewModel
    private var studentInfo: StudentInfo? = null
    private var dbHelper: CreateProfileDBHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.frame_setting, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = CreateProfileDBHelper(requireContext())
        viewModel = activity?.run {
            ViewModelProvider(this)[StudentsViewModel::class.java]
        } ?: throw Exception("Invalid")

        studentInfo = viewModel.students

        if (studentInfo != null) {
            refreshData()
        }

        settingChangeProfile.setOnClickListener {
            val changes = ChangeProfileSheet()
            changes.show(requireActivity().supportFragmentManager, "CHANGE")
        }

        settingDeleteData.setOnClickListener {
            val errorDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            errorDialog.setTitle("Hapus semua data?")
                .setCancelable(false)
                .setMessage("Semua profile akan dihapus!")
                .setPositiveButton("Hapus") { _: DialogInterface?, _: Int ->
                    GlobalScope.launch {
                        deleteAllData()
                    }
                }
                .setNegativeButton("Batal") { _: DialogInterface?, _: Int ->
                }
            val elErrorDialog = errorDialog.create()
            elErrorDialog.show()
        }
    }

    private suspend fun deleteAllData() {
        withContext(Dispatchers.IO) {
            dbHelper!!.deleteAllProfile()
            withContext(Dispatchers.Main) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun refreshData() {
        Glide.with(this).applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_mortarboard)
                .error(R.drawable.ic_mortarboard)
        )
            .load(studentInfo!!.pictureUrl)
            .transition(DrawableTransitionOptions().crossFade())
            .apply(
                RequestOptions.bitmapTransform(CircleCrop())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).override(300, 600)
            )
            .into(settingProfileIMG)

        settingName.text = studentInfo!!.name
        settingFaculty.text = studentInfo!!.faculty
        settingMajor.text = studentInfo!!.major

    }
}