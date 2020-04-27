/*
 * Copyright (c) 2019. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */

package ml.dvnlabs.unsikadu.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ml.dvnlabs.unsikadu.R
import ml.dvnlabs.unsikadu.constant.constant
import ml.dvnlabs.unsikadu.model.FinanceDetail
import ml.dvnlabs.unsikadu.ui.view.list.finance.FinanceAdapter
import ml.dvnlabs.unsikadu.util.network.APINetworkRequest
import ml.dvnlabs.unsikadu.util.network.listener.FetchDataListener
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat

class dashboardFinance : Fragment() {
    private var containers : LinearLayout? = null
    private var  loading : RelativeLayout? = null

    private var periodText : TextView? = null
    private var remainText : TextView? = null
    private var paidPercentText : TextView? = null

    private var listFinance : RecyclerView? = null

    private var progress : ProgressBar? = null
    private var token : String? = null

    private var dataFinance : ArrayList<FinanceDetail>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.frame_finance,container,false).apply {
            containers = findViewById(R.id.financeContainer)
            loading = findViewById(R.id.financeLoading)
            remainText = findViewById(R.id.financeDetail)
            paidPercentText = findViewById(R.id.paidPercent)
            listFinance = findViewById(R.id.financeList)
            progress = findViewById(R.id.paidProgress)
            periodText = findViewById(R.id.financePeriod)
        }
        loading!!.visibility = View.VISIBLE
        containers!!.visibility = View.GONE
        val prefs : SharedPreferences = context!!.getSharedPreferences("session", Context.MODE_PRIVATE)
        token = prefs.getString("token",null)
        if (token!!.isNotEmpty()){
            getFinance()
        }
        return view
    }

    private fun getFinance(){
        APINetworkRequest(activity!!,financeGet,constant.financeDetailUrl+token!!,APINetworkRequest.CODE_GET_REQUEST,null)
    }

    private fun parseFinance(data : JSONObject){
        try {
            val bill = data.getJSONArray("Bill")

            //Getting data
            for ( i in 0 until bill.length()){
                val obj : JSONObject = bill.getJSONObject(i)
                val no = obj.getInt("No")
                val period = obj.getString("Period")
                val charged = obj.getInt("Charged")
                val paid = obj.getInt("Paid")
                val first = obj.getInt("First")
                val second = obj.getInt("Second")
                val third = obj.getInt("Third")
                val remain = obj.getInt("Remain")
                val percent = obj.getInt("Percentage")
                val status = obj.getString("Status")
                dataFinance!!.add(FinanceDetail(no,period, charged, paid, first, second, third, remain, percent, status))
            }

            val length = dataFinance!!.size
            val formatter = DecimalFormat("#,###,###")

            val period = dataFinance!![length-1].period
            val remain = dataFinance!![length-1].remain
            val percent = dataFinance!![length-1].percent
            remainText!!.text = "Rp.${formatter.format(remain)}"
            paidPercentText!!.text = "$percent %"
            periodText!!.text = "Sisa Pembayaran Periode $period"
            progress!!.progress = percent

            //List initialize
            val layoutManager = LinearLayoutManager(activity!!)
            val adapter = FinanceAdapter(activity!!,dataFinance!!,R.layout.rv_finance_period)
            listFinance!!.layoutManager = layoutManager
            listFinance!!.adapter = adapter


        }catch (e : JSONException){
            e.printStackTrace()
        }
    }

    var financeGet : FetchDataListener = object : FetchDataListener{
        override fun onFetchComplete(data: String?) {
            try {
                val objects = JSONObject(data)
                if (!objects.getBoolean("Error")){
                    dataFinance = ArrayList()
                    loading!!.visibility = View.GONE
                    containers!!.visibility = View.VISIBLE
                    parseFinance(objects.getJSONObject("Finance"))
                }
            }catch (e: JSONException){
                e.printStackTrace()
            }

        }

        override fun onFetchFailure(msg: String?) {

        }

        override fun onFetchStart() {

        }
    }
}