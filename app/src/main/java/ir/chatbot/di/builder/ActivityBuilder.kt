package ir.chatbot.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector

import ir.chatbot.ui.chat.ChatFragmentProvider
import ir.chatbot.ui.chat.imageZoomableDialog.ImageZoomableDialogProvider
import ir.chatbot.ui.chatroom.ChatRoomsFragmentProvider
import ir.chatbot.ui.chatroom.createChatRoomDialog.CreateChatRoomDialogProvider
import ir.chatbot.ui.chatroom.deleteChatRoomDialog.DeleteChatRoomDialogProvider
import ir.chatbot.ui.login.LoginFragmentProvider
import ir.chatbot.ui.main.MainActivity
import ir.chatbot.ui.main.MainActivityModule


@Module
abstract class ActivityBuilder {


    @ContributesAndroidInjector(modules = [LoginFragmentProvider::class, ChatRoomsFragmentProvider::class, ChatFragmentProvider::class,
        DeleteChatRoomDialogProvider::class,
        CreateChatRoomDialogProvider::class,
        ImageZoomableDialogProvider::class,
        MainActivityModule::class])
    internal abstract fun bindMainActivity(): MainActivity
}


