package mx.edu.utez.patrullaambiental

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import mx.edu.utez.patrullaambiental.databinding.ActivityRegistroReporteBinding
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class RegistroReporte : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
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
            val longitud = binding.txtLongitud.text.toString().toFloat()
            val latitud = binding.txtLatitud.text.toString().toFloat()
            val usuarioId = userId

            if (titulo.isNotEmpty() && descripcion.isNotEmpty() && estado.isNotEmpty() && longitud != 0.0f && latitud != 0.0f && usuarioId != -1) {
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

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude

        runOnUiThread {
            binding.txtLatitud.text = "$latitude"
            binding.txtLongitud.text = "$longitude"
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
                }
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123 // Define your request code
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

    private fun registrarReporte(Reportito: JSONObject) {
        val url = "http://192.168.0.66:8080/Usuario/RCrear"
        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            Reportito,
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
