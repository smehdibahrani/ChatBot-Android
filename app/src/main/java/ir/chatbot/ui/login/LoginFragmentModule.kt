package ir.chatbot.ui.login


import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ir.chatbot.data.DataManager
import ir.chatbot.di.ViewModelProviderFactory
import ir.chatbot.utils.rx.SchedulerProvider


@Module
class LoginFragmentModule {

    @Provides
    fun loginViewModel(
        dataManager: DataManager,
        schedulerProvider: SchedulerProvider
    ): LoginViewModel {
        return LoginViewModel(dataManager, schedulerProvider)
    }




}
