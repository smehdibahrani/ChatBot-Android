package ir.chatbot.ui.main

import androidx.databinding.ObservableField
import ir.chatbot.App
import ir.chatbot.data.DataManager
import ir.chatbot.rt.MyService
import ir.chatbot.rt.MySocket

import ir.chatbot.ui.base.BaseViewModel
import ir.chatbot.utils.rx.SchedulerProvider


class MainViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<MainNavigator>(dataManager, schedulerProvider) {


    private fun updateToken() {
        if (dataManager.token != null)
            App.token = dataManager.token!!
    }


    fun startSeeding() {

        compositeDisposable.add(
            dataManager
                .userLoggedIn
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ aBoolean ->
                    decideNextFragment(aBoolean)
                }, { _ -> navigator.openLoginFragment() })
        )
    }

    private fun decideNextFragment(isLoggedIn: Boolean) {
        updateToken()
        if (isLoggedIn) {
            navigator.openChatRoomsFragment(dataManager.token)
        } else {
            navigator.openLoginFragment()
        }
    }


}
