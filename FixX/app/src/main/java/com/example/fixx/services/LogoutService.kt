package com.example.fixx.services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService


class LogoutService : JobIntentService(){

    companion object {
        private const val JOB_ID = 123

        fun enqueueWork(cxt: Context, intent: Intent){
            enqueueWork(cxt,LogoutService::class.java,JOB_ID,intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.i("TAG", "onHandleWork: service running >>>>>>>>>>>>>>>>")
    }
}