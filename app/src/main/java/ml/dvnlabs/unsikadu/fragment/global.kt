/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */


package ml.dvnlabs.unsikadu.fragment

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ml.dvnlabs.unsikadu.R

public class global{
    companion object{
        public fun addFragment(fragmentManager: FragmentManager,fragment: Fragment,id: Int ,name: String,anim_res: String){
            //val newBackStackLength = fragmentManager.backStackEntryCount + 1
            var anim_enter = 0
            var anim_exit = 0
            fragmentManager.beginTransaction().run {
                if (anim_res != "NULL") {
                    if (anim_res == "ZOOM") {
                        anim_enter = R.anim.zoom_in
                        anim_exit = R.anim.zoom_out
                    }
                    if (anim_res == "SLIDE") {
                        anim_exit = R.anim.slide_down
                        anim_enter = R.anim.slide_up
                    }
                    setCustomAnimations(anim_enter, anim_exit)
                }
                replace(id, fragment)
                val count = fragmentManager.backStackEntryCount
                Log.e("COUNTED+:", count.toString())
                if (name == "FRAGMENT_OTHER") {
                    addToBackStack(name)
                }
                commitAllowingStateLoss()
            }
        }
    }
}