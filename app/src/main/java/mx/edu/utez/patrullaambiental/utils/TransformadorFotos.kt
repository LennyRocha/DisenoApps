package mx.edu.utez.patrullaambiental.utils

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide
import mx.edu.utez.patrullaambiental.R
import java.io.File

fun cargarImagen(imageView: ImageView, fileName: String) {
    // Suponiendo que el archivo está en el directorio de caché de la aplicación
    val tempFile = File(imageView.context.cacheDir, fileName)

    // Cargar la imagen en el ImageView usando Glide
    Glide.with(imageView.context)
        .load(tempFile)
        .placeholder(R.drawable.ic_launcher_foreground)
        .error(R.drawable.ic_launcher_foreground)
        .into(imageView)
}

fun loadBase64Image(base64Image: String, imageView: ImageView) {
    // Remover el prefijo si está presente
    val cleanBase64Image = if (base64Image.startsWith("data:image/")) {
        base64Image.substring(base64Image.indexOf(",") + 1)
    } else {
        base64Image
    }

    try {
        // Decodificar la cadena Base64
        val decodedString = Base64.decode(cleanBase64Image, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

        // Configurar la imagen en el ImageView
        imageView.setImageBitmap(decodedByte)
    } catch (e: IllegalArgumentException) {
        // Manejo del error
        e.printStackTrace()
        // Puedes mostrar un mensaje de error al usuario o registrar el error
    }
}