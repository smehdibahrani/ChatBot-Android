package ir.chatbot.ui.chatroom

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ChatRoomsFragmentProvider {

    @ContributesAndroidInjector(modules = [ChatRoomsFragmentModule::class])
    internal abstract fun provideChatRoomsFragmentFactory(): ChatRoomsFragment
}
