package com.example.kotlin_project.data.workManager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.kotlin_project.data.manager.TimerManager
import com.example.kotlin_project.data.workManager.worker.TimerRunningWorker
import com.example.kotlin_project.util.helper.TimerNotificationHelper
import javax.inject.Inject

class TimerRunningWorkerFactory @Inject constructor(
    private val timerManager: TimerManager,
    private val timerNotificationHelper: TimerNotificationHelper,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return TimerRunningWorker(timerManager, timerNotificationHelper, appContext, params)
    }
}
