package mx.edu.utez.patrullaambiental

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Space
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import mx.edu.utez.patrullaambiental.databinding.ActivityInicioBinding

class Inicio : AppCompatActivity() {
    private val fragment1 = Frag_inicio()
    private val fragment2 = Frag_noticias()
    private val fragment3 = Frag_mapa()
    private lateinit var binding : ActivityInicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar)

        binding.myToolbar.setLogo(R.drawable.logito)
        val space = Space(this).apply {
            layoutParams = Toolbar.LayoutParams(32, Toolbar.LayoutParams.WRAP_CONTENT)
        }
        binding.myToolbar.addView(space, 2)
        binding.myToolbar.setTitle(". "+getString(R.string.app_name_spaced))

        //supportActionBar?.setDisplayShowTitleEnabled(false)

        val sharedPreferences = getSharedPreferences("archivo", Context.MODE_PRIVATE)
        val valor = sharedPreferences.getString("usuario","#")

        // Configuramos el listener para el BottomNavigationView
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item1 -> {
                    loadFragment(fragment1)
                    true
                }
                R.id.item3 -> {
                    loadFragment(fragment2)
                    true
                }
                R.id.item2 -> {
                    loadFragment(fragment3)
                    true
                }
                else -> false
            }
        }

        // Cargar el primer fragmento por defecto
        loadFragment(fragment1)
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.contenedor.id, fragment)
        transaction.commit()
    }


    //Asigne el menú a la activity (se va a ver)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Asigna el funcionaminto al menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.logut_pregunta))
                builder.setMessage(getString(R.string.logut_body))
                builder.setIcon(R.drawable.logo_verde)
                //builder.setView(dialogView)
                builder.setPositiveButton(getString(R.string.si)) { dialog, _ ->
                    val sharedPreferences = getSharedPreferences("archivo", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()){
                        remove("usuario")
                        remove("apellido")
                        remove("password")
                        remove("fotoP")
                        remove("email")
                        remove("estado")
                        remove("id")
                        commit()
                    }
                    val intent = Intent(this@Inicio,MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                }
                builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.cancel()
                }
                builder.show()
            }
            R.id.perfil -> {
                val intent = Intent(this@Inicio,MiPerfil::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            R.id.config -> {
                val intent = Intent(this@Inicio,SettingsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}