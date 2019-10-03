/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.view.list.grade

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.activity.DashboardActivity
import ml.dvnlabs.unsikadu.constant.constant
import ml.dvnlabs.unsikadu.model.GradeCourseSemester
import ml.dvnlabs.unsikadu.model.GradeSemester
import ml.dvnlabs.unsikadu.util.network.APINetworkRequest
import ml.dvnlabs.unsikadu.util.network.listener.FetchDataListener
import net.cachapa.expandablelayout.ExpandableLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class gradeAsSemesterListHolder(context: Context,itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener{
    var mcontext = context
    var token : String? = null

    var smtText : TextView = itemView.findViewById(R.id.rvGradeSmt)
    var smtNameText : TextView = itemView.findViewById(R.id.rvGradeSmtName)
    var smtGPAText : TextView = itemView.findViewById(R.id.rvGradeSmtGPA)
    var smtDetailText : TextView = itemView.findViewById(R.id.rvGradeSmtDetail)
    var smtMkText : TextView = itemView.findViewById(R.id.rvGradeSmtMk)
    var smtSksText : TextView = itemView.findViewById(R.id.rvGradeSmtSks)

    var expandSmt : ExpandableLayout = itemView.findViewById(R.id.rvGradeMoreExpand)
    var container : MaterialCardView = itemView.findViewById(R.id.rvGradeSmtMain)

    var list : RecyclerView = itemView.findViewById(R.id.rvGradeCourseList)
    var loading : RelativeLayout = itemView.findViewById(R.id.rvGradeCourseLoading)

    var data  : GradeSemester?=  null

    var grade : ArrayList<GradeCourseSemester>? = null

    init {
        val prefs : SharedPreferences = mcontext.getSharedPreferences("session", Context.MODE_PRIVATE)
        token = prefs.getString("token",null)
        grade = ArrayList()
        container.setOnClickListener(this)
    }

    fun bind(model : GradeSemester?){
        if (model != null){
            data = model
            smtText.text = model.Semester
            smtNameText.text = model.Periodic
            smtGPAText.text = model.Cumulative.toString()
            smtDetailText.text = model.Detail
            val mk = model.NumCourse.toString()
            val sks = model.Credit.toString()
            smtMkText.text = "MK : $mk"
            smtSksText.text = "SKS : $sks"
        }
    }
    private fun getCourseGradeOnSemester(){
        val year = data!!.Year
        val quart = data!!.Quart
        APINetworkRequest(mcontext,getCourse,constant.gradeDetailUrl+"$year/$quart/$token",APINetworkRequest.CODE_GET_REQUEST,null)
    }

    private fun parseCourseScore(array : JSONArray){
        try {
            for (i in 0 until  array.length()){
                val objects = array.getJSONObject(i)
                val name = objects.getString("CourseName")
                val gradeLetter = objects.getString("GradeLetter")
                val num = objects.getInt("Num")
                val credit = objects.getInt("Credit")
                val point = objects.getInt("GradePoint")
                val avail = objects.getDouble("Availability")
                val quiz = objects.getDouble("Quiz")
                val assignment = objects.getDouble("Assignment")
                val mid = objects.getDouble("MidTerm")
                val last = objects.getDouble("LastTerm")
                grade!!.add(GradeCourseSemester(name,gradeLetter,num,credit,avail,quiz,assignment,mid,last,point))
            }
            val adapter = gradeCourseListAdapter(mcontext,grade!!,R.layout.rv_grade_course)
            val layoutManager = LinearLayoutManager(mcontext)
            list.layoutManager = layoutManager
            list.adapter = adapter

        }catch (e : JSONException){
            e.printStackTrace()
        }
    }

    var getCourse : FetchDataListener = object : FetchDataListener{
        override fun onFetchComplete(data: String?) {
            try {
                val dataa = JSONObject(data)
                if (!dataa.getBoolean("Error")){
                    loading.visibility = View.GONE
                    list.visibility = View.VISIBLE
                    val grade = dataa.getJSONObject("Grade")
                    //println(grade.getJSONArray("Data").toString(1))
                    parseCourseScore(grade.getJSONArray("Data"))
                }else{
                    val dashboardInfo : DashboardActivity = context as DashboardActivity
                    dashboardInfo.notAuthorizedAlert()
                }
            }catch (e : JSONException){
                e.printStackTrace()
            }
        }

        override fun onFetchFailure(msg: String?) {

        }

        override fun onFetchStart() {
            loading.visibility = View.VISIBLE
            list.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        expandSmt.toggle()
        if(expandSmt.isExpanded){
            if (token!!.isNotEmpty()){
                getCourseGradeOnSemester()
            }
        }else{
            grade!!.clear()
        }
    }

}