package mx.edu.utez.patrullaambiental

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import mx.edu.utez.patrullaambiental.databinding.ActivityRegistroReporteBinding
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class RegistroReporte : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroReporteBinding

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                Glide.with(this)
                    .load(result)
                    .into(binding.imgReporte)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroReporteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("archivo", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("id", -1)
        binding.imgReporte.setOnClickListener {
            // Abrir la galería para seleccionar una imagen
            galleryLauncher.launch("image/*")
        }

        binding.btnRegistrar.setOnClickListener {
            val titulo = binding.edtTitulo.text.toString()
            val descripcion = binding.edtDescripcion.text.toString()
            val estado = "Activo"
            val longitud = 100.1
            val latitud = 200.1
            val usuarioId = userId

            if (titulo.isNotEmpty() && descripcion.isNotEmpty()) {
                val imagenBase64 = convertirImagenABase64(binding.imgReporte)
                if (imagenBase64 != null) {
                    val reporte = JSONObject().apply {
                        put("titulo", titulo)
                        put("descripcion", descripcion)
                        put("estado", estado)
                        put("longitud", longitud)
                        put("latitud", latitud)
                        put("imagen", imagenBase64)
                        put("usuario", JSONObject().apply { put("id", usuarioId) })
                    }
                    registrarReporte(reporte)
                } else {
                    Toast.makeText(this, "Selecciona una imagen", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun convertirImagenABase64(imageView: ImageView): String? {
        return try {
            val drawable = imageView.drawable as BitmapDrawable
            val bitmap = drawable.bitmap
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            val bytes = outputStream.toByteArray()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e("RegistroReporte", "Error al convertir imagen a Base64", e)
            null
        }
    }

    private fun registrarReporte(reporte: JSONObject) {
        val url = "http://192.168.0.166:8080/Usuario/RCrear"
        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            reporte,
            { response ->
                Log.d("RegistroReporte", "Reporte registrado: $response")
                Toast.makeText(this, "Reporte registrado con éxito", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Inicio::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            },
            { error ->
                Log.e("RegistroReporte", "Error al registrar reporte", error)
                Toast.makeText(this, "Error al registrar el reporte", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}
