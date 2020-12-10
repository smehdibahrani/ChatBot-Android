package ir.chatbot.ui.chatroom.deleteChatRoomDialog


import dagger.Module
import dagger.Provides
import ir.chatbot.data.DataManager
import ir.chatbot.utils.rx.SchedulerProvider


@Module
class DeleteChatRoomDialogModule {

    @Provides
    internal fun provideRateUsViewModel(
        dataManager: DataManager,
        schedulerProvider: SchedulerProvider
    ): DeleteChatRoomViewModel {
        return DeleteChatRoomViewModel(dataManager, schedulerProvider)
    }
}
