package com.andreikslpv.flickrrecent.presentation.wm

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class PhotoWorker(
    context: Context,
    workerParams: WorkerParameters
) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        //makeStatusNotification("notification", applicationContext)
        println("I/o notification")
        return Result.success()
    }

    @SuppressLint("CheckResult")
    private fun runHeavyTask() {
//        val appContext = applicationContext
        //makeStatusNotification("notification", applicationContext)
        println("I/o notification")
    }
}