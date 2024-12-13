package mx.edu.utez.patrullaambiental

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.media.RingtoneManager.EXTRA_RINGTONE_PICKED_URI
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.edu.utez.patrullaambiental.databinding.ActivitySettingsBinding
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar2)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.btnSalir.setOnClickListener {
            val intent = Intent(this@SettingsActivity,Inicio::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)

        val savedLanguage = sharedPreferences.getString("Language", "es")
        when (savedLanguage) {
            "es" -> binding.rbEs.isChecked = true
            "en" -> binding.rbEn.isChecked = true
            "fr" -> binding.rbFr.isChecked = true
        }

        binding.switchMode.isChecked = sharedPreferences.getBoolean("DarkMode", false)

        binding.switchMode.setOnCheckedChangeListener { _, isChecked ->
            setDarkMode(isChecked)
        }

        binding.radGru.setOnCheckedChangeListener { group, checkedId ->
            //val seleccionado = group.checkedRadioButtonId
            val elegido = when(checkedId){
                R.id.rbEs -> "es"
                R.id.rbEn -> "en"
                R.id.rbFr -> "fr"
                else -> "Sin elección"
            }
            if (sharedPreferences.getString("Language", "es") != elegido) {
                changeLanguage(elegido)
                sharedPreferences.edit().putString("Language", elegido).apply()
            }
        }

        val dialogView = layoutInflater.inflate(R.layout.layout_new_pass, null)
        val caja = dialogView.findViewById<EditText>(R.id.edtActualizadorContra)

        binding.txtCambiarContra.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.newPassText))
            builder.setIcon(R.drawable.logo_verde)
            builder.setView(dialogView)
            builder.setPositiveButton(getString(R.string.guardar)) { dialog, _ ->
                Toast.makeText(this@SettingsActivity,getString(R.string.succesSave), Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Manejar la acción de la flecha aquí
                val intent = Intent(this@SettingsActivity,Inicio::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setDarkMode(enabled: Boolean) {
        // Guardar el estado del modo oscuro en SharedPreferences
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("DarkMode", enabled)
        editor.apply()

        // Aplicar el tema
        val mode = if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }


    private fun changeLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate() // Reinicia la actividad
    }
}