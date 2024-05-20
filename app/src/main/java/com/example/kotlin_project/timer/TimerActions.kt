package com.example.kotlin_project.timer

interface TimerActions {
    fun setCountDownTimer() {}
    fun setHour(hour: Int) {}
    fun setMinute(minute: Int) {}
    fun setSecond(second: Int) {}
    fun reset() {}
    fun pause() {}
    fun start() {}
}
