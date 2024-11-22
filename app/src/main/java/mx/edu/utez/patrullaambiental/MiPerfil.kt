package mx.edu.utez.patrullaambiental

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import mx.edu.utez.patrullaambiental.databinding.ActivityInicioBinding
import mx.edu.utez.patrullaambiental.databinding.ActivityMiPerfilBinding

class MiPerfil : AppCompatActivity() {
    private lateinit var binding : ActivityMiPerfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar2)
    }


    //Asigne el menú a la activity (se va a ver)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_mi_perfil, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Asigna el funcionaminto al menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemReturn ->{
                val intent = Intent(this@MiPerfil,Inicio::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}