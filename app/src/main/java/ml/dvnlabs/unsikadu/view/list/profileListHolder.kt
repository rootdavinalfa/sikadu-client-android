/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.view.list

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.util.database.model.ProfileList

class ProfileListHolder(context: Context?, view: View,adapter: ProfileListAdapter) : RecyclerView.ViewHolder(view),View.OnClickListener{

    var mcontext = context

    var userName : TextView? = view.findViewById(R.id.profileName)
    var idUser : TextView? = view.findViewById(R.id.profileId)
    var profileImg : ImageView? = view.findViewById(R.id.profileImage)
    var profileDelete : ImageView? = view.findViewById(R.id.profileDelete)
    var profileContainer : CardView? = view.findViewById(R.id.rvProfilelistContainer)

    var adapters = adapter
    var models : ProfileList? = null

    //var listener : DeleteProfileListener? = null

    init {
        itemView.setOnClickListener(this)
        //listener = context as DeleteProfileListener
    }
    fun bindList(model : ProfileList){
        models = model
        profileDelete!!.setOnClickListener(View.OnClickListener {
            if (models!!.Selected == 1){
                profileDelete!!.isClickable = false
                Toast.makeText(mcontext,"Can't delete item",Toast.LENGTH_SHORT).show()
            }else{
                adapters.onDeleteProfileId(models!!.ids)
            }
            //listener!!.deleteIDProfile(model.ids)
        })
        Glide.with(mcontext!!).applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_mortarboard)
                .error(R.drawable.ic_mortarboard))
            .load(model.profileImgUrl)
            .transition(DrawableTransitionOptions().crossFade())
            .apply(
                RequestOptions.bitmapTransform(CircleCrop())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).override(300,300))
            .into(profileImg!!)
        userName!!.text = model.studentName
        idUser!!.text = model.studentId
        if (model.Selected == 1){
            profileContainer!!.setBackgroundColor(mcontext!!.resources.getColor(R.color.secondaryDarkColor))
            userName!!.setTextColor(mcontext!!.resources.getColor(R.color.primaryTextColor))
            idUser!!.setTextColor(mcontext!!.resources.getColor(R.color.primaryTextColor))
        }
    }

    override fun onClick(v: View?) {
        if (models!!.Selected == 1){
            v!!.isClickable = false
            Toast.makeText(mcontext,"Can't be selected item",Toast.LENGTH_SHORT).show()
        }else{
            adapters.onSelectProfileId(models!!.ids)
        }
    }
}