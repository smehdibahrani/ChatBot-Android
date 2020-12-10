package ir.chatbot.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ir.chatbot.rt.MyService


@Module
abstract class ServiceBuilderModule {

    // Note: Add all your application service classes here
    // As an example I am adding IdentifyAppClearedFromRecentService
    @ContributesAndroidInjector
     abstract fun bindMyService(): MyService
}