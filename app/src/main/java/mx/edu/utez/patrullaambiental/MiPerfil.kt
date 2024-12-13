package mx.edu.utez.patrullaambiental

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import mx.edu.utez.patrullaambiental.databinding.ActivityInicioBinding
import mx.edu.utez.patrullaambiental.databinding.ActivityMiPerfilBinding
import java.io.File
import java.util.UUID

class MiPerfil : AppCompatActivity() {
    private lateinit var binding : ActivityMiPerfilBinding
    private lateinit var photo : File
    private lateinit var queue : RequestQueue

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

    fun actualizarPerfil(uriel : Uri){
        //
    }
}