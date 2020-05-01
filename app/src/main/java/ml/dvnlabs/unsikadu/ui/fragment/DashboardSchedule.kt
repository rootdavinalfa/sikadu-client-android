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
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.sort.SortState
import com.jaredrummler.materialspinner.MaterialSpinner
import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.constant.constant
import ml.dvnlabs.unsikadu.model.ScheduleData
import ml.dvnlabs.unsikadu.model.ScheduleDetail
import ml.dvnlabs.unsikadu.ui.activity.DashboardActivity
import ml.dvnlabs.unsikadu.ui.view.tableview.schedule.scheduleTableAdapter
import ml.dvnlabs.unsikadu.util.network.APINetworkRequest
import ml.dvnlabs.unsikadu.util.network.listener.FetchDataListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DashboardSchedule : Fragment(){

    var selectYear : MaterialSpinner? = null
    var selectContainer : LinearLayout? = null
    var scheduleView : TableView? = null
    var loadingContainer : RelativeLayout? = null
    var semesterAttended : TextView? = null

    var token : String? = null
    var mcontext : Context? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mcontext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.frame_schedulestudent, container, false).apply {
            selectYear = findViewById(R.id.scheduleSelectYear)
            selectContainer = findViewById(R.id.scheduleSelectContainer)
            scheduleView = findViewById(R.id.scheduleTableView)
            loadingContainer = findViewById(R.id.scheduleLoadingContainer)
            semesterAttended = findViewById(R.id.scheduleAttended)
        }
        initial()
        return view
    }
    private fun initial(){
        selectContainer!!.visibility = View.GONE
        loadingContainer!!.visibility = View.VISIBLE
        scheduleView!!.visibility = View.GONE

        val prefs : SharedPreferences = requireContext().getSharedPreferences("session",Context.MODE_PRIVATE)
        token = prefs.getString("token",null)
        if (token!!.isNotEmpty()){
            getYearList()
        }
    }


    private fun getYearList(){
        APINetworkRequest(requireActivity(),getAcademicYear,constant.scheduleUrl+token,APINetworkRequest.CODE_GET_REQUEST,null)
    }

    private fun getScheduleDetail(year : String,quart : String){
        APINetworkRequest(requireActivity(),getScheduleDetail,constant.scheduleUrl+"$year/$quart/"+token,APINetworkRequest.CODE_GET_REQUEST,null)
    }

    private fun parseSchedule(sch : JSONArray){
        try {
            val listName = ArrayList<String>()
            val listYearData = ArrayList<ScheduleData>()
            for (i in 0 until sch.length()){
                val listObj = sch.getJSONObject(i)
                val name = listObj.getString("Name")
                val year = listObj.getString("Year")
                val quar = listObj.getString("Quart")
                listName.add(name)
                listYearData.add(ScheduleData(name,year,quar))
            }
            selectYear!!.setItems(listName)
            selectYear!!.setOnItemSelectedListener { view, position, id, item ->
                val selyears = listYearData[position].Year
                val selquarts = listYearData[position].Quart
                getScheduleDetail(selyears,selquarts)
            }
            //Default
            val selyear = listYearData[listYearData.size-1].Year
            val selquart = listYearData[listYearData.size-1].Quart
            selectYear!!.selectedIndex = listYearData.size-1
            getScheduleDetail(selyear,selquart)
        }catch (e : JSONException){
            e.printStackTrace()
        }
    }
    private fun parseScheduleDetail(info : JSONArray){
        try {
            val listSchedule = ArrayList<ScheduleDetail>()
            for (i in 0 until info.length()){
                val listData = info.getJSONObject(i)
                val courn = listData.getString("CourseName")
                val clas = listData.getString("Class")
                val room = listData.getString("Room")
                val lecture = listData.getString("Lecturer")
                val day = listData.getString("Days")
                val smter = listData.getInt("Semester")
                val times = listData.getJSONObject("Times")
                val fromtime = times.getString("FromTime")
                val totime = times.getString("ToTime")
                listSchedule.add(ScheduleDetail(courn,clas,room,lecture,day,smter,fromtime,totime))
            }

            //Sorting by fromTime Ascending 18 -> 20
            fun sortScheduleSelector( d : ScheduleDetail) : String = d.fromTime
            listSchedule.sortBy {
                sortScheduleSelector(it)
            }

            val data: ml.dvnlabs.unsikadu.ui.view.tableview.schedule.model.ScheduleData =
                ml.dvnlabs.unsikadu.ui.view.tableview.schedule.model.ScheduleData(listSchedule)
            val adapter = scheduleTableAdapter(requireContext())
            scheduleView!!.adapter = adapter
            adapter.setAllItems(data.columnHeader,data.rowHeader,data.cell)
            scheduleView!!.sortColumn(4,SortState.DESCENDING)

        }catch (e : JSONException){
            e.printStackTrace()
        }

    }

    private var getAcademicYear : FetchDataListener = object : FetchDataListener{
        override fun onFetchComplete(data: String?) {
            try {
                val objects = JSONObject(data!!)
                if (!objects.getBoolean("Error")){
                    selectContainer!!.visibility = View.VISIBLE
                    val info : JSONObject = objects.getJSONObject("Info")
                    val attended : Int = info.getInt("SemesterAttended")
                    semesterAttended!!.text = "Semester yang sudah dilalui : $attended"
                    parseSchedule(info.getJSONArray("List"))

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

        }
    }
    private var getScheduleDetail : FetchDataListener = object : FetchDataListener{
        override fun onFetchComplete(data: String?) {
            try {
                val objects = JSONObject(data!!)
                if (!objects.getBoolean("Error")){
                    scheduleView!!.visibility = View.VISIBLE
                    loadingContainer!!.visibility = View.GONE
                    val info : JSONObject = objects.getJSONObject("Info")
                    parseScheduleDetail(info.getJSONArray("Data"))
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
            loadingContainer!!.visibility = View.VISIBLE
            scheduleView!!.visibility = View.GONE

        }
    }
}