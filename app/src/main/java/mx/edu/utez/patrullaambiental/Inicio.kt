package mx.edu.utez.patrullaambiental

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mx.edu.utez.patrullaambiental.databinding.ActivityInicioBinding

class Inicio : AppCompatActivity() {
    private lateinit var binding : ActivityInicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar)

        val sharedPreferences = getSharedPreferences("archivo", Context.MODE_PRIVATE)
        val valor = sharedPreferences.getString("usuario","#")
        binding.txtBienvenido.setText(resources.getString(R.string.bienve)+" "+valor)


    }

    //Asigne el menú a la activity (se va a ver)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Asigna el funcionaminto al menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("¿Cerrar sesión?")
                builder.setMessage("Estas a punto de cerrar sesión, \n ¿Deseas continuar?")
                //builder.setIcon(R.drawable.icon)
                //builder.setView(dialogView)
                builder.setPositiveButton("Si") { dialog, _ ->
                    val sharedPreferences = getSharedPreferences("archivo", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()){
                        remove("usuario")
                        commit()
                    }
                    finish()
                }
                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }
                builder.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}