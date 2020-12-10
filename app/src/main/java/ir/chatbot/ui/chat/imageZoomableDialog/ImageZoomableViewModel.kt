package ir.chatbot.ui.chat.imageZoomableDialog

import ir.chatbot.data.DataManager
import ir.chatbot.ui.base.BaseViewModel
import ir.chatbot.utils.rx.SchedulerProvider


class ImageZoomableViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<ImageZoomableCallback>(dataManager, schedulerProvider) {

    fun onLaterClick() {
        navigator.dismissDialog()
    }

    fun onSubmitClick() {

        navigator.submit()
    }

}
