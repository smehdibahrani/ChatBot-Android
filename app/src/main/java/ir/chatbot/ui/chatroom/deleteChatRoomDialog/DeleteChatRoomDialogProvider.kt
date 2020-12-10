package ir.chatbot.ui.chatroom.deleteChatRoomDialog

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class DeleteChatRoomDialogProvider {

    @ContributesAndroidInjector(modules = [DeleteChatRoomDialogModule::class])
    abstract fun provideDeleteChatRoomDialogFactory(): DeleteChatRoomDialog
}
