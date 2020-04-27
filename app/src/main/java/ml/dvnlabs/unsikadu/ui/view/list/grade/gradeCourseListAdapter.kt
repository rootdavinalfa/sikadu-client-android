/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.ui.view.list.grade

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ml.dvnlabs.unsikadu.model.GradeCourseSemester

class gradeCourseListAdapter(context: Context, data : ArrayList<GradeCourseSemester>, rvLayoutRes : Int) : RecyclerView.Adapter<gradeCourseListHolder>(){

    var context : Context? = context
    var datas: ArrayList<GradeCourseSemester>? = data
    var itemRes : Int = rvLayoutRes
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): gradeCourseListHolder {
        val view : View = LayoutInflater.from(context).inflate(itemRes,parent,false)
        return gradeCourseListHolder(context!!,view)
    }
    
    override fun onBindViewHolder(holder: gradeCourseListHolder, position: Int) {
        val model : GradeCourseSemester = datas!![holder.adapterPosition]
        holder.bind(model)
    }
    
    override fun getItemCount(): Int {
        if (datas!= null){
            return datas!!.size
        }
        return 0
    }
}