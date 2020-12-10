package ir.chatbot.utils

import android.content.Context
import android.graphics.Typeface
import android.util.Log

import java.lang.reflect.Field


object FontUtils {
    private var IransansFont: Typeface? = null
    private var IransansBoldFont: Typeface? = null
    var IRANSansMobile = "IRANSansMobile.ttf"
    var IRANSansMobilePath = "fonts/IRANSansMobile.ttf"
    var IRANSansMobile_Bold = "IRANSansMobile_Bold.ttf"


    fun IransansBold(context: Context): Typeface {
        if (IransansBoldFont == null)
            IransansBoldFont =
                Typeface.createFromAsset(context.assets, "fonts/IRANSansMobile_Bold.ttf")
        return IransansBoldFont!!
    }


    fun Iransans(context: Context): Typeface {
        if (IransansFont == null)
            IransansFont = Typeface.createFromAsset(context.assets, "fonts/IRANSansMobile.ttf")
        return IransansFont!!
    }


    fun Awesome(context: Context): Typeface {
        return Typeface.createFromAsset(context.assets, "fonts/fontawesome.ttf")
    }


    fun overrideFont(
        context: Context,
        defaultFontNameToOverride: String,
        customFontFileNameInAssets: String
    ) {
        try {
            val customFontTypeface =
                Typeface.createFromAsset(context.assets, customFontFileNameInAssets)

            val defaultFontTypefaceField =
                Typeface::class.java.getDeclaredField(defaultFontNameToOverride)
            defaultFontTypefaceField.isAccessible = true
            defaultFontTypefaceField.set(null, customFontTypeface)
        } catch (e: Exception) {
            Log.e(
                "font",
                "Can not set custom font $customFontFileNameInAssets instead of $defaultFontNameToOverride"
            )
        }

    }
}
