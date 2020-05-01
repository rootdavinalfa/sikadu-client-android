/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.base

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected open fun setupFragment(
        fragment: Fragment?,
        layout: Int,
        tags: String,
        bundle: Bundle?
    ) {
        bundle?.let {
            fragment!!.arguments = bundle
        }

        supportFragmentManager
            .beginTransaction()
            .replace(layout, fragment!!, tags)
            .commit()

    }

    protected open fun stackedFragment(fragment: Fragment?, layout: Int, tags: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(layout, fragment!!)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Change status bar color by using following parameter
     *
     * [activity] Fill with requester Activity eg:MainActivity or using 'this'
     *
     * [color] Fill with ID
     *
     * [iconLight] Statement using light icon or not
     *
     * */
    protected open fun changeStatusBar(activity: Activity, color: Int, iconLight: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = window.decorView.systemUiVisibility
            if (iconLight) {
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.decorView.systemUiVisibility = flags
            activity.window.statusBarColor = getColor(color)
        }
    }

}