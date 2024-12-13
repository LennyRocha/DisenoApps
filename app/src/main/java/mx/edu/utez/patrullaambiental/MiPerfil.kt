package mx.edu.utez.patrullaambiental

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import mx.edu.utez.patrullaambiental.adapter.Usuario
import mx.edu.utez.patrullaambiental.databinding.ActivityInicioBinding
import mx.edu.utez.patrullaambiental.databinding.ActivityMiPerfilBinding
import mx.edu.utez.patrullaambiental.utils.cargarImagen
import mx.edu.utez.patrullaambiental.utils.loadBase64Image
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID

class MiPerfil : AppCompatActivity() {
    private lateinit var binding : ActivityMiPerfilBinding
    private lateinit var photo : File

    private val cameraLauncher =
        registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ){ result ->
            if(result){
                Glide.with(this)
                    .load(photo.toUri())
                    .into(binding.imgPerfil)
                //actualizarPerfil(photo.toUri())
            }
        }

    private val galleryLauncher =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ){ result ->
            if(result != null){
                Glide.with(this)
                    .load(result)
                    .into(binding.imgPerfil)
                //actualizarPerfila(result)
            }
        }

    private fun createBlankTempFile() : File{
        val tempFolder = File(applicationContext.filesDir,"photos")
        tempFolder.deleteRecursively()  // Eliminar contenido previo
        tempFolder.mkdir()  // Crear nuevo directorio
        return File(tempFolder,"tempPhoto${UUID.randomUUID()}.jpeg")  // Crear el archivo con un nombre único
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar2)

        val sharedPreferences = getSharedPreferences("archivo", Context.MODE_PRIVATE)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
            //title = getString(R.string.miPer)
        }

        binding.imgPerfil.apply {
            layoutParams = LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT )
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        loadPerfil()

        binding.edtNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.any { char -> char.isDigit() }) {
                        binding.edtNombre.error = getString(R.string.no_nums)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtApellido.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.any { char -> char.isDigit() }) {
                        binding.edtApellido.error = getString(R.string.no_nums)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnCambiaFoto.setOnClickListener {
            // Crear el BottomSheetDialog
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.menu_foto_perfil, null)

            // Configurar las opciones del menú
            val btnCameraVista = view.findViewById<ImageButton>(R.id.btnCamara1)
            val btnGalleryVista = view.findViewById<ImageButton>(R.id.btnGaleria1)
            val cerrarBottomSheet = view.findViewById<ImageButton>(R.id.btnCerrarMenucito)

            btnCameraVista.setOnClickListener {
                // Acción para abrir la cámara
                photo = createBlankTempFile()  // Ahora se inicializa primero
                val uri = FileProvider.getUriForFile(
                    this,
                    "mx.edu.utez.patrullaambiental",  // Asegúrate de usar tu package name correcto
                    photo
                )
                cameraLauncher.launch(uri)
                bottomSheetDialog.dismiss()
            }

            btnGalleryVista.setOnClickListener {
                // Acción para abrir la galería
                galleryLauncher.launch("image/*")
                bottomSheetDialog.dismiss()
            }

            cerrarBottomSheet.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }

        binding.btnActualizarUsuario.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.put_pregunta))
            builder.setMessage(getString(R.string.put_pregunta))
            builder.setIcon(R.drawable.logo_verde)
            //builder.setView(dialogView)
            builder.setPositiveButton(getString(R.string.si)) { dialog, _ ->
                if(binding.edtNombre.text.isEmpty() || binding.edtApellido.text.isEmpty()){
                    Toast.makeText(this,getString(R.string.empty_login), Toast.LENGTH_SHORT).show()
                }else{
                    val user = Usuario(
                        sharedPreferences.getInt("id",0),
                        binding.edtNombre.text.toString(),
                        binding.edtApellido.text.toString(),
                        sharedPreferences.getString("password","#")!!,
                        sharedPreferences.getString("email","#")!!,
                        sharedPreferences.getString("estado","#")!!,
                        CrearBase64(binding.imgPerfil),
                    )
                    actualizarPerfil(user)
                    recreate()
                }
            }
            builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Manejar la acción de la flecha aquí
                val intent = Intent(this@MiPerfil,Inicio::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun actualizarPerfil(bro : Usuario){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.1.67:8080/UActualizar/${bro.Id}"
        val metodo = Request.Method.POST
        val body  = JSONObject()
        body.put(
            "nombre",
            bro.nombre
        )
        body.put(
            "apellido",
            bro.apellido
        )
        body.put(
            "password",
            bro.password
        )
        body.put(
            "email",
            bro.email
        )
        body.put(
            "estado",
            bro.estado
        )
        body.put(
            "fotoP",
            bro.imagen
        )
        val listener = Response.Listener<JSONObject> { resulttado ->
            Log.d("Insercion", "USUARIO INSERTADO")
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            Toast.makeText(this,getString(R.string.exito_registro), Toast.LENGTH_SHORT).show()
        }
        val errorListener = Response.ErrorListener { error -> Toast.makeText(this, getString(R.string.error_registro)+" "+error.message, Toast.LENGTH_SHORT).show() }
        val request = JsonObjectRequest(metodo, url, body, listener, errorListener)
        queue.add(request)
    }

    fun loadPerfil(){
        val sharedPreferences = getSharedPreferences("archivo", Context.MODE_PRIVATE)
        val imagenPerf : String = sharedPreferences.getString("fotoP","#").toString()
        if (imagenPerf != "#"){
            loadBase64Image(imagenPerf,binding.imgPerfil)
        }else{
            binding.imgPerfil.setImageResource(R.drawable.perfil_default)
        }
        binding.edtNombre.setText(sharedPreferences.getString("usuario","#"))
        binding.edtApellido.setText(sharedPreferences.getString("apellido","#"))
        binding.txtCorreoNuevo.setText(sharedPreferences.getString("email","#"))

    }

    fun CrearBase64(img : ImageView) : String{
        val drawable = img.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val bytes = outputStream.toByteArray()

        println(bytes.toString())
        val base64String = Base64.encodeToString(bytes, Base64.DEFAULT)
        println(base64String)

        return base64String
    }

}