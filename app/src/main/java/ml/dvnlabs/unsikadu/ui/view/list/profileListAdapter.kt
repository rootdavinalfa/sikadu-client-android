/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.ui.view.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ml.dvnlabs.unsikadu.util.database.model.ProfileList

class ProfileListAdapter(context : Context, data : ArrayList<ProfileList>, rvLayoutRes : Int) : RecyclerView.Adapter<ProfileListHolder>(){
    var context : Context? = context
    var datas: ArrayList<ProfileList>? = data
    var itemRes : Int = rvLayoutRes

    var onDeleteProfileId : (Int) -> Unit = {}
    var onSelectProfileId : (Int) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileListHolder {
        val view : View = LayoutInflater.from(context).inflate(itemRes,parent,false)
        return ProfileListHolder(context,view,this)
    }

    override fun onBindViewHolder(holder: ProfileListHolder, position: Int) {
        val model : ProfileList = datas!![holder.adapterPosition]
        holder.bindList(model)
    }

    override fun getItemCount(): Int {
        if (datas != null){
            return datas!!.size
        }
        return 0
    }
}