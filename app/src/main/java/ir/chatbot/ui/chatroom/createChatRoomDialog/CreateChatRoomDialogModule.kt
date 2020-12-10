package ir.chatbot.ui.chatroom.createChatRoomDialog


import dagger.Module
import dagger.Provides
import ir.chatbot.data.DataManager
import ir.chatbot.utils.rx.SchedulerProvider


@Module
class CreateChatRoomDialogModule {

    @Provides
    internal fun provideCreateChatRoomViewModel(
        dataManager: DataManager,
        schedulerProvider: SchedulerProvider
    ): CreateChatRoomViewModel {
        return CreateChatRoomViewModel(dataManager, schedulerProvider)
    }
}
