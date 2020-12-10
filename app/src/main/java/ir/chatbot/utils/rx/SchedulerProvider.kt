package ir.chatbot.utils.rx

import io.reactivex.Scheduler


interface SchedulerProvider {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler

}
