package ai.travel.app.home.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
    return try {
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null // Return null in case of an error
    }
}