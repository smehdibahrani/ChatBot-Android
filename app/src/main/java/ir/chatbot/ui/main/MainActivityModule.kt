package ir.chatbot.ui.main

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ir.chatbot.di.ViewModelProviderFactory
import ir.chatbot.data.DataManager
import ir.chatbot.utils.rx.SchedulerProvider


@Module
class MainActivityModule {

    @Provides
    internal fun mainViewModelProvider(mainViewModel: MainViewModel): ViewModelProvider.Factory {
        return ViewModelProviderFactory(mainViewModel)
    }

    @Provides
    internal fun provideMainViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider): MainViewModel {
        return MainViewModel(dataManager, schedulerProvider)
    }
}
