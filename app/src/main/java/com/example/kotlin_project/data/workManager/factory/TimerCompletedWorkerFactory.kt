package com.example.kotlin_project.data.workManager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.kotlin_project.data.manager.TimerManager
import com.example.kotlin_project.data.workManager.worker.TimerCompletedWorker
import com.example.kotlin_project.util.helper.MediaPlayerHelper
import com.example.kotlin_project.util.helper.TimerNotificationHelper
import javax.inject.Inject

class TimerCompletedWorkerFactory @Inject constructor(
    private val mediaPlayerHelper: MediaPlayerHelper,
    private val timerNotificationHelper: TimerNotificationHelper,
    private val timerManager: TimerManager,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return TimerCompletedWorker(mediaPlayerHelper, timerNotificationHelper, timerManager, appContext, params)
    }
}
