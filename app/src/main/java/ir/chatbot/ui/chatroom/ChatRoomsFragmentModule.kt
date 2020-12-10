package ir.chatbot.ui.chatroom


import dagger.Module
import dagger.Provides
import ir.chatbot.data.DataManager
import ir.chatbot.utils.rx.SchedulerProvider


@Module
class ChatRoomsFragmentModule {

    @Provides
    internal fun chatRoomsViewModel(
        dataManager: DataManager,
        schedulerProvider: SchedulerProvider
    ): ChatRoomsViewModel {
        return ChatRoomsViewModel(dataManager, schedulerProvider)
    }



}
