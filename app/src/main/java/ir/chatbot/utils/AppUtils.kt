package ir.chatbot.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import ir.chatbot.R


object AppUtils {

    fun openPlayStoreForApp(context: Context) {
        val appPackageName = context.packageName
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        context
                            .resources
                            .getString(R.string.app_name) + appPackageName
                    )
                )
            )
        } catch (e: android.content.ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        context
                            .resources
                            .getString(R.string.app_name) + appPackageName
                    )
                )
            )
        }

    }
}
