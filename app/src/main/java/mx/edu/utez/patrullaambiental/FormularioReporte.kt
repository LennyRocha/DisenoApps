package mx.edu.utez.patrullaambiental

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import mx.edu.utez.patrullaambiental.databinding.ActivityFormularioReporteBinding
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.format

class FormularioReporte : AppCompatActivity(), LocationListener {

    private lateinit var binding: ActivityFormularioReporteBinding
    private lateinit var locationManager: LocationManager
    private lateinit var photo: File
    private lateinit var queue: RequestQueue

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { result ->
        if (result) {
            Glide.with(this)
                .load(photo.toUri())
                .into(binding.imgPreview)
        }
    }

    private val galleryLauncher =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { result ->
            if (result != null) {
                Glide.with(this)
                    .load(result)
                    .into(binding.imgPreview)
            }
        }

    private fun createBlankTempFile(): File {
        val tempFolder = File(applicationContext.filesDir, "photos")
        tempFolder.deleteRecursively()
        tempFolder.mkdir()
        val tempPhoto = File(tempFolder, "tempPhoto${UUID.randomUUID()}.jpg")
        return tempPhoto
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioReporteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        queue = Volley.newRequestQueue(this)

        binding.btnEnviar.setOnClickListener{
            val titulo = binding.edtTitulo.text.toString()
            val desc = binding.edtDesc.text.toString()
            val latitud = binding.txtLatitud.text.toString()
            val longitud = binding.txtLongitud.text.toString()
            val estado = binding.txtEstado.text.toString()
            val imagen = binding.imgPreview.toString()

            if (titulo.isNotEmpty() && desc.isNotEmpty() && latitud.isNotEmpty() && longitud.isNotEmpty() && estado.isNotEmpty() && imagen.isNotEmpty()) {
                val url = "http://192.168.111.81:8080/Usuario/RCrear"
                val metodo = Request.Method.POST
                val body = JSONObject()
                body.put("titulo", titulo)
                body.put("desc", desc)
                body.put("latitud", latitud)
                body.put("longitud", longitud)
                body.put("estado", estado)
                body.put("imagen", imagen)

                val listener = Response.Listener<JSONObject> { resultado ->
                    try {
                        Log.d("insertado", "Usuario insertado correctamente")
                    }catch (e: Exception){
                        Toast.makeText(this, "ALGO PASO", Toast.LENGTH_SHORT).show()
                    }
                }

                val errorListener = Response.ErrorListener {
                    error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()

                }

                val request = JsonObjectRequest(metodo,url,body,listener,errorListener)
                queue.add(request)
                Toast.makeText(this, "Reporte enviado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.edtTitulo.setText(intent.getStringExtra("titulo"))
        binding.edtDesc.setText(intent.getStringExtra("descripcion"))


        binding.btnEnviar.setOnClickListener {
            Toast.makeText(this, "Reporte enviado", Toast.LENGTH_SHORT).show()
        }

        binding.btnCamara.setOnClickListener {
            photo = createBlankTempFile()
            val uri = FileProvider.getUriForFile(
                this,
                "mx.edu.utez.camara4e",
                photo
            )
            cameraLauncher.launch(uri)
        }



        binding.btnGaleria.setOnClickListener {
            galleryLauncher.launch("image/*")
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
            binding.txtLatitud.text = "Latitud: $latitude"
            binding.txtLongitud.text = "Longitud: $longitude"
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
}