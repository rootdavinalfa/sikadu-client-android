/*
 * Copyright (c) 2020. dvnlabs.ml , Davin Alfarizky Putra Basudewa
 * Email : dbasudewa@gmail.com / moshi2_davin@dvnlabs.ml
 * UnSikadu source code for Android (tm) ,
 * Internal License Only,NOT FOR REDISTRIBUTE
 */
package ml.dvnlabs.unsikadu.util.network

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class RequestQueueVolley private constructor(private val mContext: Context) {
    private var requestQueue: RequestQueue?
    private fun getRequestQueue(): RequestQueue? {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue =
                Volley.newRequestQueue(mContext.applicationContext)
        }
        return requestQueue
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.retryPolicy = DefaultRetryPolicy(
            5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        getRequestQueue()!!.add(req).tag = "API"
    }

    fun stopAllRequest() {
        getRequestQueue()!!.cancelAll("API")
    }

    fun clearCache() {
        requestQueue!!.cache.clear()
    }

    fun removeCache(key: String?) {
        requestQueue!!.cache.remove(key)
    }

    companion object {
        private var mInstance: RequestQueueVolley? = null

        @Synchronized
        fun getInstance(context: Context): RequestQueueVolley? {
            if (mInstance == null) {
                mInstance = RequestQueueVolley(context)
            }
            return mInstance
        }
    }

    init {
        requestQueue = getRequestQueue()
    }
}