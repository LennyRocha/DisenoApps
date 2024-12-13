package mx.edu.utez.patrullaambiental

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import mx.edu.utez.patrullaambiental.adapter.Usuario
import mx.edu.utez.patrullaambiental.databinding.ActivitySignupBinding
import mx.edu.utez.u2p2.model.MultipartRequest
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID

class Signup : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    private lateinit var photo : File
    private lateinit var queue: RequestQueue

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

    private fun createBlankTempFile() : File {
        val tempFolder = File(applicationContext.filesDir,"photos")
        tempFolder.deleteRecursively()  // Eliminar contenido previo
        tempFolder.mkdir()  // Crear nuevo directorio
        return File(tempFolder,"tempPhoto${UUID.randomUUID()}.jpeg")  // Crear el archivo con un nombre único
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        queue = Volley.newRequestQueue(this)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.edtNewNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.any { char -> char.isDigit() }) {
                        binding.edtNewNombre.error = getString(R.string.no_nums)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.edtNewApellido.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.any { char -> char.isDigit() }) {
                        binding.edtNewApellido.error = getString(R.string.no_nums)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val typeface = ResourcesCompat.getFont(this, R.font.sour_gummy_extra_bold)
        with(binding){
            txtTitulo.typeface = typeface
            txtContraNueva.typeface = typeface
            txtCorreoNuevo.typeface = typeface
            txtNombreNuevo.typeface = typeface
            txtApellidoNuevo.typeface = typeface
            txtConfirmaContra.typeface = typeface
        }

        binding.btnRegistrame.setOnClickListener{
            val nombre = binding.edtNewNombre.text.toString()
            val apellido = binding.edtNewApellido.text.toString()
            val correo = binding.edtNewCorreo.text.toString()
            val contrasena= binding.edtNewContra.text.toString()
            val confirmarContra= binding.edtConfirContra.text.toString()
            val estado = "Activo"

            if(nombre.isNotEmpty()&&apellido.isNotEmpty()&&correo.isNotEmpty()&&contrasena.isNotEmpty()&&confirmarContra.isNotEmpty()){
                if(contrasena.equals(confirmarContra)){
                    val nuevoUsu = Usuario(1,nombre,apellido,correo,contrasena,estado)
                    registrarUsuario(binding.imgPerfil,nuevoUsu)
                }else{
                    Toast.makeText(this,"Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this,"Llena todos los campos", Toast.LENGTH_SHORT).show()
            }

        }


        binding.uploadIcon.setOnClickListener{
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

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
            //title = getString(R.string.inicioReg)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Manejar la acción de la flecha aquí
                val intent = Intent(this@Signup, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun registrarUsuario(imageView: ImageView, bro: Usuario) {
        val url = "http://192.168.0.166:8080/Admin/UCrear"

        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val bytes = outputStream.toByteArray()

        val params = mapOf(
            "nombre" to bro.nombre,
            "apellido" to bro.apellido,
            "password" to bro.password,
            "email" to bro.email,
            "estado" to bro.estado
        )


        val multipartRequest = MultipartRequest(
            url,
            bytes,
            params,
            { resultado ->
                Log.d("Insercion", "USUARIO INSERTADO")
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        ) { error ->
            Toast.makeText(this, "Error al enviar datos: ${error.message}", Toast.LENGTH_SHORT).show()
        }

        queue.add(multipartRequest)
    }
}