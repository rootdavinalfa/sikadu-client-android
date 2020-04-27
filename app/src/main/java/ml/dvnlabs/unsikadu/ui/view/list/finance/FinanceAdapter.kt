/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.ui.view.list.finance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ml.dvnlabs.unsikadu.model.FinanceDetail

class FinanceAdapter(context: Context, data : ArrayList<FinanceDetail>, rvLayoutRes : Int) : RecyclerView.Adapter<FinanceHolder>() {
    var context : Context? = context
    var datas: ArrayList<FinanceDetail>? = data
    var itemRes : Int = rvLayoutRes

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinanceHolder {
        val view : View = LayoutInflater.from(context).inflate(itemRes,parent,false)
        return FinanceHolder(context!!,view)
    }

    override fun onBindViewHolder(holder: FinanceHolder, position: Int) {
        val model : FinanceDetail = datas!![holder.adapterPosition]
        holder.bindFinance(model)
    }

    override fun getItemCount(): Int {
        return if (datas!!.isNotEmpty()){
            datas!!.size
        }else{
            0
        }
    }

}