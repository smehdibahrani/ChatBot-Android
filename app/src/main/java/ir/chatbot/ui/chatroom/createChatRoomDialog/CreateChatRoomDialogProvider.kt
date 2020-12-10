package ir.chatbot.ui.chatroom.createChatRoomDialog

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class CreateChatRoomDialogProvider {

    @ContributesAndroidInjector(modules = [CreateChatRoomDialogModule::class])
    internal abstract fun provideCreateChatRoomDialogFactory(): CreateChatRoomDialog
}
