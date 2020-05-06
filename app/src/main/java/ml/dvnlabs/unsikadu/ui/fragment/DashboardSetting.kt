/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
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

import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.model.StudentInfo
import ml.dvnlabs.unsikadu.viewmodel.StudentsViewModel

class DashboardSetting : Fragment() {
    private lateinit var viewModel: StudentsViewModel
    private var studentInfo: StudentInfo? = null
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
        viewModel = activity?.run {
            ViewModelProvider(this)[StudentsViewModel::class.java]
        } ?: throw Exception("Invalid")

        studentInfo = viewModel.students

        if (studentInfo != null) {
            refreshData()
        }
        settingLogoutProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Not Implemented", Toast.LENGTH_LONG).show()
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