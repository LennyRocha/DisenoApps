package mx.edu.utez.patrullaambiental

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import mx.edu.utez.patrullaambiental.databinding.ActivityInicioBinding
import mx.edu.utez.patrullaambiental.databinding.ActivityMiPerfilBinding

class MiPerfil : AppCompatActivity() {
    private lateinit var binding : ActivityMiPerfilBinding
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
            val snack =
                Snackbar.make(binding.main, "Funcionalidad en construcción...", Snackbar.LENGTH_SHORT)
            snack.setAction("Aceptar") {
                snack.dismiss()
            }
            snack.show()
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
}