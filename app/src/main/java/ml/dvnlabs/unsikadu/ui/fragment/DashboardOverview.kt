/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.frame_overview.*
import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.base.BaseFragment
import ml.dvnlabs.unsikadu.base.BaseViewModel
import ml.dvnlabs.unsikadu.model.StudentInfo
import ml.dvnlabs.unsikadu.viewmodel.StudentsViewModel


class DashboardOverview : BaseFragment() {
    private lateinit var viewModel: StudentsViewModel
    private lateinit var base: BaseViewModel

    var studentInfo: StudentInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frame_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            studentInfo = it.getParcelable("student")
        }

        viewModel = activity?.run {
            ViewModelProvider(this)[StudentsViewModel::class.java]
        } ?: throw   Exception("Invalid")

        base = activity?.run {
            ViewModelProvider(this)[BaseViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        studentInfo = viewModel.students!!
        if (studentInfo != null) {
            refreshData()
        }
    }

    private fun refreshData() {
        overviewName.text = studentInfo!!.name
        overviewFaculty.text = studentInfo!!.faculty
        overviewMajor.text = studentInfo!!.major

        overviewGradeContainer.setOnClickListener {
            base.selectedItem(R.id.bottomMenuGrade)
        }

        overviewFinance.setOnClickListener {
            base.selectedItem(R.id.bottomMenuFinance)
        }

        overviewSchedule.setOnClickListener {
            base.selectedItem(R.id.bottomMenuSchedule)
        }

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
            .into(overviewProfileIMG)
    }
}