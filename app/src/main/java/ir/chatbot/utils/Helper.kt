package ir.chatbot.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.text.format.DateFormat

import androidx.core.content.ContextCompat
import java.util.*
import kotlin.system.measureTimeMillis

object Helper {
    fun color2Bitmap(context: Context, res: Int): Bitmap {
        val color =
            ContextCompat.getColor(context, res)

        val bmp = Bitmap.createBitmap(72, 72, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(color)
        return bmp
    }

    fun longToDateFormat(millisecond: Long): String {
        // or you already have long value of date, use this instead of milliseconds variable.
        return DateFormat.format("MM/dd/yyyy\nHH:mm:ss", Date(millisecond)).toString()
    }

    fun dateTimeNow(): String {
        val tz: TimeZone = TimeZone.getTimeZone("GMT+04:30")
        val millisecond = Calendar.getInstance(tz).time.time
        // or you already have long value of date, use this instead of milliseconds variable.
        return DateFormat.format("MM/dd/yyyy\nHH:mm:ss", Date(millisecond)).toString()
    }

}
