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
import ml.dvnlabs.unsikadu.model.GradeSemester

class gradeAsSemesterListAdapter(context: Context, data : ArrayList<GradeSemester>, rvLayoutRes : Int) : RecyclerView.Adapter<gradeAsSemesterListHolder>(){

    var context : Context? = context
    var datas: ArrayList<GradeSemester>? = data
    var itemRes : Int = rvLayoutRes

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): gradeAsSemesterListHolder {
        val view : View = LayoutInflater.from(context).inflate(itemRes,parent,false)
        return gradeAsSemesterListHolder(context!!,view)
    }

    override fun getItemCount(): Int {
        if (datas!= null){
            return datas!!.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: gradeAsSemesterListHolder, position: Int) {
        val model : GradeSemester = datas!![holder.adapterPosition]
        holder.bind(model)
    }


}