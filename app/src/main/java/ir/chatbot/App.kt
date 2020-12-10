package ir.chatbot

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import ir.chatbot.di.component.DaggerAppComponent
import ir.chatbot.rt.MyService
import ir.chatbot.rt.MySocket
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import javax.inject.Inject
import dagger.android.AndroidInjector
import ir.chatbot.data.DataManager
import android.graphics.Color
import android.os.Build
import dagger.android.HasServiceInjector
import ir.chatbot.utils.Notify


class App : Application(), HasActivityInjector ,HasServiceInjector{

    companion object {
        var token: String = ""
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context


    }


    @Inject
    lateinit var dataManager: DataManager

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var serviceDispatchingAndroidInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var mCalligraphyConfig: CalligraphyConfig

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return activityDispatchingAndroidInjector
    }

    override fun serviceInjector(): AndroidInjector<Service> {
        return serviceDispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        val token = dataManager.token
        if (token != null && token.isNotEmpty()) {
            MySocket.instance.initialize(applicationContext, token, MyService())
        }
        CalligraphyConfig.initDefault(mCalligraphyConfig)
        createChannel()


    }


    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val context = this.applicationContext
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(Notify.NotificationChannelID, "Background Service Demo Notification Channel", importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#e8334a")
            notificationChannel.description = "NotificationChannel Desc"
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

}