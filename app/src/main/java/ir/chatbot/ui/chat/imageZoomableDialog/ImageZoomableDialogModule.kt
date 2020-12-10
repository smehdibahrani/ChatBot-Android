package ir.chatbot.ui.chat.imageZoomableDialog


import dagger.Module
import dagger.Provides
import ir.chatbot.data.DataManager
import ir.chatbot.utils.rx.SchedulerProvider


@Module
class ImageZoomableDialogModule {

    @Provides
    internal fun provideImageZoomableViewModel(
        dataManager: DataManager,
        schedulerProvider: SchedulerProvider
    ): ImageZoomableViewModel {
        return ImageZoomableViewModel(dataManager, schedulerProvider)
    }
}
