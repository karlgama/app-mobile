package net.daan.odontoapp.utils

import android.graphics.Bitmap
import android.util.Base64
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream
import java.security.MessageDigest

object HashUtils {
    fun genereateUniqueHash(): String {
        val timeStamp = System.currentTimeMillis().toString()
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(timeStamp.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(hash, 0).trim().replace("/", "")
    }

    fun generateBase64FromImageView(imageView: ImageView): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        imageView.drawable.toBitmap()
            .compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}