package mx.edu.utez.patrullaambiental

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import mx.edu.utez.patrullaambiental.databinding.ActivitySignupBinding

class Signup : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        val typeface = ResourcesCompat.getFont(this, R.font.sour_gummy_extra_bold)

        with(binding){
            txtTitulo.typeface = typeface
            txtRolNuevo.typeface = typeface
            txtContraNueva.typeface = typeface
            txtCorreoNuevo.typeface = typeface
            txtNombreNuevo.typeface = typeface
            txtApellidoNuevo.typeface = typeface
            txtConfirmaContra.typeface = typeface
        }

        binding.uploadIcon.setOnClickListener{
            val snack =
                Snackbar.make(binding.main, "Funcionalidad en construcción...", Snackbar.LENGTH_SHORT)
            snack.setAction("Aceptar") {
                snack.dismiss()
            }
            snack.show()
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
}