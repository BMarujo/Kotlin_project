package com.example.kotlin_project.di

import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import com.example.kotlin_project.data.workManager.factory.ChildWorkerFactory
import com.example.kotlin_project.data.workManager.factory.TimerCompletedWorkerFactory
import com.example.kotlin_project.data.workManager.factory.TimerRunningWorkerFactory
import com.example.kotlin_project.data.workManager.factory.WrapperWorkerFactory
import com.example.kotlin_project.data.workManager.worker.TimerCompletedWorker
import com.example.kotlin_project.data.workManager.worker.TimerRunningWorker
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

@InstallIn(SingletonComponent::class)
@Module
abstract class WorkerModule {

    @Binds
    abstract fun bindWorkerFactoryModule(workerFactory: WrapperWorkerFactory): WorkerFactory



    @Binds
    @IntoMap
    @WorkerKey(TimerRunningWorker::class)
    abstract fun bindTimerRunningWorker(timerRunningWorkerFactory: TimerRunningWorkerFactory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(TimerCompletedWorker::class)
    abstract fun bindTimerCompletedWorker(timerCompletedWorkerFactory: TimerCompletedWorkerFactory): ChildWorkerFactory

}
