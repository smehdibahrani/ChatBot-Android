package ir.chatbot.ui.chat.imageZoomableDialog

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ImageZoomableDialogProvider {

    @ContributesAndroidInjector(modules = [ImageZoomableDialogModule::class])
    abstract fun provideImageZoomableDialogFactory(): ImageZoomableDialog
}
