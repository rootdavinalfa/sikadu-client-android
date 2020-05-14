/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.model.StudentInfo
import ml.dvnlabs.unsikadu.ui.activity.DashboardActivity

class DashboardInfo() : Fragment(){
    var imageProfile : ImageView? = null

    var nameProfile : TextView?= null
    var idProfile : TextView? = null
    var borOnProfile : TextView? = null
    var bornDateProfile : TextView? = null
    var genderProfile : TextView? = null
    var religionProfile : TextView? = null
    var addressProfile : TextView? = null
    var facultyProfile : TextView? = null
    var majorProfile : TextView? = null
    var levelProfile : TextView? = null
    var classProfile : TextView? = null
    var groupProfile : TextView? = null
    var activeProfile : TextView? = null

    var studentInfo : StudentInfo? = null
    lateinit var dashboardActivity: DashboardActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashboardActivity = context as DashboardActivity
        studentInfo = dashboardActivity.studentInfo
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.frame_infostudent,container,false)
        imageProfile = view.findViewById(R.id.dashInfoImage)
        nameProfile = view.findViewById(R.id.dashInfoName)
        idProfile = view.findViewById(R.id.dashInfoStudentId)
        borOnProfile = view.findViewById(R.id.dashInfoBornOn)
        bornDateProfile = view.findViewById(R.id.dashInfoBornDate)
        genderProfile = view.findViewById(R.id.dashInfoGender)
        religionProfile = view.findViewById(R.id.dashInfoReligion)
        addressProfile = view.findViewById(R.id.dashInfoAddress)
        facultyProfile = view.findViewById(R.id.dashInfoEduFaculty)
        majorProfile = view.findViewById(R.id.dashInfoEduMajor)
        levelProfile = view.findViewById(R.id.dashInfoEduLevel)
        classProfile = view.findViewById(R.id.dashInfoEduClass)
        groupProfile = view.findViewById(R.id.dashInfoEduGroup)
        activeProfile = view.findViewById(R.id.dashInfoEduActive)
        bindData()
        return view
    }

    private fun bindData(){
        nameProfile!!.text = studentInfo!!.name
        idProfile!!.text = studentInfo!!.npm
        borOnProfile!!.text = studentInfo!!.placeBorn
        bornDateProfile!!.text = studentInfo!!.bornDate
        genderProfile!!.text = studentInfo!!.gender
        religionProfile!!.text = studentInfo!!.religion
        addressProfile!!.text = studentInfo!!.address
        facultyProfile!!.text = studentInfo!!.faculty
        majorProfile!!.text = studentInfo!!.major
        levelProfile!!.text = studentInfo!!.degree
        classProfile!!.text = studentInfo!!.clas
        groupProfile!!.text = studentInfo!!.group
        activeProfile!!.text = studentInfo!!.status
        Glide.with(this).applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_mortarboard)
                .error(R.drawable.ic_mortarboard))
            .load(studentInfo!!.pictureUrl)
            .transition(DrawableTransitionOptions().crossFade())
            .apply(
                RequestOptions.bitmapTransform(RoundedCorners(10))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).override(300,600))
            .into(imageProfile!!)
    }
}