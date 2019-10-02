/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.button.MaterialButton
import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.activity.DashboardActivity
import ml.dvnlabs.unsikadu.constant.constant
import ml.dvnlabs.unsikadu.model.GradeSemester
import ml.dvnlabs.unsikadu.util.network.APINetworkRequest
import ml.dvnlabs.unsikadu.util.network.listener.FetchDataListener
import ml.dvnlabs.unsikadu.view.list.grade.gradeAsSemesterListAdapter
import net.cachapa.expandablelayout.ExpandableLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class dashboardGrade : Fragment(){
    var loading : RelativeLayout? = null
    var containerLay : LinearLayout? = null
    var semesterList : RecyclerView? = null
    var chart : LineChart? = null
    var expandPerformance : ExpandableLayout? = null



    var ipkText : TextView?= null
    var sksText : TextView?= null
    var mkText : TextView?= null
    var expandButton : MaterialButton? = null

    var token : String? = null

    var semesterData : ArrayList<GradeSemester>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.frame_gradestudent,container,false).apply {
            loading = findViewById(R.id.gradeLoading)
            containerLay = findViewById(R.id.gradeContainer)
            ipkText = findViewById(R.id.gradeIPK)
            sksText = findViewById(R.id.gradeSKS)
            mkText = findViewById(R.id.gradeMK)
            semesterList = findViewById(R.id.gradeSemesterList)
            chart = findViewById(R.id.gradePerformanceChart)
            expandButton = findViewById(R.id.gradeShowPerformance)
            expandPerformance = findViewById(R.id.gradeExpandPerformance)
        }
        val prefs : SharedPreferences = context!!.getSharedPreferences("session", Context.MODE_PRIVATE)
        token = prefs.getString("token",null)
        if (token!!.isNotEmpty()){
            getSemester()
        }
        return view
    }

    private fun getSemester(){
        APINetworkRequest(activity!!,getSemesterData,constant.gradeSummaryUrl+token,APINetworkRequest.CODE_GET_REQUEST,null)
    }
    private fun parseGradeData(data : JSONArray) = try {
        var sks = 0
        var mk = 0
        for (i in 0 until data.length()){
            val objects = data.getJSONObject(i)
            val skss =objects.getInt("Credit")
            val nummk = objects.getInt("NumCourse")
            val cumulative = objects.getDouble("Cumulative")
            val year = objects.getString("Year")
            val quart = objects.getString("Quart")
            val semester = objects.getString("Semester")
            val period = objects.getString("Periodic")
            val detail = objects.getString("Detail")
            semesterData!!.add(GradeSemester(year,quart,semester,period,detail,nummk,skss,cumulative))
            sks += skss
            mk += nummk
        }
        val valuesChart : ArrayList<Entry> = ArrayList()
        for (i in 0 until semesterData!!.size){
            valuesChart.add(Entry(i.toFloat(), semesterData!![i].Cumulative.toFloat()))
        }
        val set : LineDataSet
        chart!!.xAxis.setDrawGridLines(false)
        chart!!.axisRight.isEnabled = false
        chart!!.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart!!.description.text = ""
        chart!!.legend.isEnabled = true
        chart!!.xAxis.textColor = context!!.resources.getColor(R.color.primaryTextColor)
        chart!!.axisLeft.textColor = context!!.resources.getColor(R.color.primaryTextColor)
        if (chart!!.data != null && chart!!.data.dataSetCount > 0){
            set = (chart!!.data.getDataSetByIndex(0) as LineDataSet)
            set.values = valuesChart
            chart!!.data.notifyDataChanged()
            chart!!.notifyDataSetChanged()
        }else{
            set = LineDataSet(valuesChart,"Academic Performance")
            set.setDrawFilled(true)
            set.setDrawValues(true)
            set.setDrawCircles(false)
            set.fillAlpha = 255
            set.valueTextColor = context!!.resources.getColor(R.color.primaryTextColor)
            set.color = context!!.resources.getColor(R.color.redpink)
            set.fillColor =context!!.resources.getColor(R.color.redpink)

            set.valueTextSize = 9F
            set.mode = LineDataSet.Mode.CUBIC_BEZIER
            val dataSet : ArrayList<ILineDataSet> = ArrayList()
            dataSet.add(set)
            val lineData = LineData(dataSet)
            chart!!.data = lineData

        }
        expandButton!!.setOnClickListener {
            expandPerformance!!.toggle()
        }

        mkText!!.text = "Totak MK: $mk"
        sksText!!.text = "Totak SKS: $sks"
        val adapter = gradeAsSemesterListAdapter(activity!!,semesterData!!,R.layout.rv_grade_main)
        val linearLayoutManager = LinearLayoutManager(activity!!)
        semesterList!!.layoutManager = linearLayoutManager
        semesterList!!.adapter = adapter

    }catch (e : JSONException){
        e.printStackTrace()
    }

    private var getSemesterData : FetchDataListener = object : FetchDataListener{
        override fun onFetchComplete(data: String?) {
            try {
                val objects = JSONObject(data!!)
                if (!objects.getBoolean("Error")){
                    semesterData = ArrayList()
                    containerLay!!.visibility = View.VISIBLE
                    loading!!.visibility = View.GONE
                    val grade : JSONObject = objects.getJSONObject("Grade")
                    val gpa = grade.getDouble("GPA")
                    ipkText!!.text = gpa.toString()
                    parseGradeData(grade.getJSONArray("Data"))
                }else{
                    val dashboardInfo : DashboardActivity = activity as DashboardActivity
                    dashboardInfo.notAuthorizedAlert()
                }
            }catch (e : JSONException){
                e.printStackTrace()
            }
        }

        override fun onFetchFailure(msg: String?) {

        }

        override fun onFetchStart() {
            containerLay!!.visibility = View.GONE
            loading!!.visibility = View.VISIBLE

        }
    }
}