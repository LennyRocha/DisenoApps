package mx.edu.utez.patrullaambiental

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import mx.edu.utez.patrullaambiental.databinding.ActivityLoginBinding
import java.util.Locale

class Login : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_login)

        val sharedPreferences = getSharedPreferences("archivo", Context.MODE_PRIVATE)

        binding.btnAcceder.setOnClickListener{
            if(binding.edtUsuario.text.toString().isNotEmpty() && binding.edtContra.text.toString().isNotEmpty()){
                if(binding.edtUsuario.text.toString().toLowerCase(Locale.ROOT) == "leo" && binding.edtContra.text.toString() == "123"){
                    with(sharedPreferences.edit()){
                        putString("usuario",binding.edtUsuario.text.toString())
                        commit()
                    }
                    val intent = Intent(this@Login, Inicio::class.java)
                    startActivity(intent)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }else{
                    Toast.makeText(this@Login,"El usuario y una contraseña no son correctos cuyeyo", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@Login,"Escribe un usuario y una contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        binding.linkSignUp.setOnClickListener{
            val snack =
                Snackbar.make(binding.main, "Funcionalidad en construcción...", Snackbar.LENGTH_SHORT)
            snack.setAction("Aceptar") {
                snack.dismiss()
            }
            snack.show()
        }
    }
}