package ir.chatbot.ui.chat


import dagger.Module
import dagger.Provides
import ir.chatbot.data.DataManager
import ir.chatbot.utils.rx.SchedulerProvider


@Module
class ChatFragmentModule {

    @Provides
    internal fun chatViewModel(
        dataManager: DataManager,
        schedulerProvider: SchedulerProvider
    ): ChatViewModel {
        return ChatViewModel(dataManager, schedulerProvider)
    }


}
