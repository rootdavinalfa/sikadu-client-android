/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    /*selectedItem is used for defining menu ID when bottomnavview or going to
    specific menu*/
    val selectedItem = MutableLiveData<Int>()
    fun selectedItem(selected: Int) {
        selectedItem.value = selected
    }
}