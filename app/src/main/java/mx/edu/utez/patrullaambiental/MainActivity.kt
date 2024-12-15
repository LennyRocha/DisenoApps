package mx.edu.utez.patrullaambiental

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.edu.utez.patrullaambiental.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Simular un retardo
        Handler().postDelayed({
            // Obtener SharedPreferences
            val sharedPreferences = getSharedPreferences("archivo", Context.MODE_PRIVATE)

            // Leer usuario
            val usuario = sharedPreferences.getString("usuario", "#")

            //Leer tema
            val darkPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            val isDarkModeEnabled = darkPreferences.getBoolean("DarkMode", false)

            // Aplicar el tema
            val mode = if (isDarkModeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(mode)

            // Leer configuración de idioma y establecerlo
            val idioma = darkPreferences.getString("Language", "es")
            val locale = Locale(idioma ?: "es")
            println(locale)
            Locale.setDefault(locale)
            val config = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

            // Decidir la siguiente actividad
            if (usuario != "#") {
                // Iniciar la actividad principal después del retardo
                val intent = Intent(this@MainActivity, Inicio::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                Toast.makeText(this@MainActivity, getString(R.string.bienve)+" "+usuario, Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@MainActivity, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            finish()
        }, 3000) // 3000 milisegundos = 3 segundos
    }
}