/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.view.list.finance

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ml.dvnlabs.unsikadu.model.FinanceDetail
import ml.dvnlabs.unsikadu.R
import net.cachapa.expandablelayout.ExpandableLayout
import java.text.DecimalFormat

class FinanceHolder(context: Context,view: View) : RecyclerView.ViewHolder(view),View.OnClickListener{

    private var mcontex = context

    private var numText : TextView = view.findViewById(R.id.rvFinanceNum)
    private var periodText : TextView = view.findViewById(R.id.rvFinancePeriod)
    private var statusText : TextView = view.findViewById(R.id.rvFinanceStatus)

    private var chargedText : TextView = view.findViewById(R.id.rvFinanceCharged)
    private var firstText : TextView = view.findViewById(R.id.rvFinanceFirst)
    private var secondText : TextView = view.findViewById(R.id.rvFinanceSecond)
    private var thirdText : TextView = view.findViewById(R.id.rvFinanceThird)
    private var paidText : TextView = view.findViewById(R.id.rvFinancePaid)
    private var remainText : TextView = view.findViewById(R.id.rvFinanceRemain)

    private var expandMore : ExpandableLayout = view.findViewById(R.id.rvFinanceExpand)



    init {
        view.setOnClickListener(this)
    }


    fun bindFinance(data : FinanceDetail){
        numText.text = data.no.toString()
        periodText.text = data.period
        val status = data.status
        if (status.equals("Lunas")){
            statusText.setTextColor(mcontex.resources.getColor(R.color.gren))
        }else{
            statusText.setTextColor(mcontex.resources.getColor(R.color.red))
        }

        statusText.text = status
        //Format to 3,000,000
        val formatter = DecimalFormat("#,###,###")
        val charged = data.charged
        val paid = data.paid
        val first = data.first
        val second = data.second
        val third = data.third
        val remain = data.remain

        chargedText.text = "Rp.${formatter.format(charged)}"
        paidText.text = "Rp.${formatter.format(paid)}"
        firstText.text = "Rp.${formatter.format(first)}"
        secondText.text = "Rp.${formatter.format(second)}"
        thirdText.text = "Rp.${formatter.format(third)}"
        remainText.text = "Rp.${formatter.format(remain)}"


    }

    override fun onClick(v: View?) {
        expandMore.toggle()
    }
}