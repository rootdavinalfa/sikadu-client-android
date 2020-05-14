/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.ui.fragment

import android.content.Context
import android.content.SharedPreferences
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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.frame_overview.*
import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.base.BaseFragment
import ml.dvnlabs.unsikadu.base.BaseViewModel
import ml.dvnlabs.unsikadu.constant.constant
import ml.dvnlabs.unsikadu.model.GradeSemester
import ml.dvnlabs.unsikadu.model.StudentInfo
import ml.dvnlabs.unsikadu.ui.activity.DashboardActivity
import ml.dvnlabs.unsikadu.util.network.APINetworkRequest
import ml.dvnlabs.unsikadu.util.network.listener.FetchDataListener
import ml.dvnlabs.unsikadu.viewmodel.StudentsViewModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class DashboardOverview : BaseFragment() {
    private lateinit var viewModel: StudentsViewModel
    private lateinit var base: BaseViewModel

    var studentInfo: StudentInfo? = null
    var token: String? = null
    var semesterData: ArrayList<GradeSemester>? = null

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

        val prefs: SharedPreferences =
            requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        token = prefs.getString("token", null)

        if (token!!.isNotEmpty()) {
            getSemester()
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


    private fun getSemester() {
        APINetworkRequest(
            requireActivity(), getSemesterData,
            constant.gradeSummaryUrl + token,
            APINetworkRequest.CODE_GET_REQUEST, null
        )
    }

    private fun parseGradeData(data: JSONArray) = try {
        var sks = 0
        var mk = 0
        for (i in 0 until data.length()) {
            val objects = data.getJSONObject(i)
            val skss = objects.getInt("Credit")
            val nummk = objects.getInt("NumCourse")
            val cumulative = objects.getDouble("Cumulative")
            val year = objects.getString("Year")
            val quart = objects.getString("Quart")
            val semester = objects.getString("Semester")
            val period = objects.getString("Periodic")
            val detail = objects.getString("Detail")
            semesterData!!.add(
                GradeSemester(
                    year,
                    quart,
                    semester,
                    period,
                    detail,
                    nummk,
                    skss,
                    cumulative
                )
            )
            sks += skss
            mk += nummk
        }
        val valuesChart: ArrayList<Entry> = ArrayList()
        for (i in 0 until semesterData!!.size) {
            valuesChart.add(Entry(i.toFloat(), semesterData!![i].Cumulative.toFloat()))
        }
        val set: LineDataSet
        overviewGrade.xAxis.setDrawGridLines(false)
        overviewGrade.axisRight.isEnabled = false
        overviewGrade.xAxis.position = XAxis.XAxisPosition.BOTTOM
        overviewGrade.description.text = ""
        overviewGrade.legend.isEnabled = true
        overviewGrade.xAxis.textColor =
            requireContext().resources.getColor(R.color.primaryTextColor)
        overviewGrade.axisLeft.textColor =
            requireContext().resources.getColor(R.color.primaryTextColor)
        if (overviewGrade.data != null && overviewGrade.data.dataSetCount > 0) {
            set = (overviewGrade.data.getDataSetByIndex(0) as LineDataSet)
            set.values = valuesChart
            overviewGrade.data.notifyDataChanged()
            overviewGrade.notifyDataSetChanged()
        } else {
            set = LineDataSet(valuesChart, "Academic Performance")
            set.setDrawFilled(true)
            set.setDrawValues(true)
            set.setDrawCircles(false)
            set.fillAlpha = 255
            set.valueTextColor = requireContext().resources.getColor(R.color.secondaryTextColor)
            set.color = requireContext().resources.getColor(R.color.redpink)
            set.fillColor = requireContext().resources.getColor(R.color.redpink)

            set.valueTextSize = 9F
            set.mode = LineDataSet.Mode.CUBIC_BEZIER
            val dataSet: ArrayList<ILineDataSet> = ArrayList()
            dataSet.add(set)
            val lineData = LineData(dataSet)
            overviewGrade.data = lineData

        }

    } catch (e: JSONException) {
        e.printStackTrace()
    }

    private var getSemesterData: FetchDataListener = object : FetchDataListener {
        override fun onFetchComplete(data: String?) {
            try {
                val objects = JSONObject(data!!)
                if (!objects.getBoolean("Error")) {
                    semesterData = ArrayList()
                    val grade: JSONObject = objects.getJSONObject("Grade")
                    parseGradeData(grade.getJSONArray("Data"))
                } else {
                    val dashboardInfo: DashboardActivity = activity as DashboardActivity
                    dashboardInfo.notAuthorizedAlert()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        override fun onFetchFailure(msg: String?) {

        }

        override fun onFetchStart() {

        }
    }
}