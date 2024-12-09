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
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }else if (binding.edtUsuario.text.toString().toLowerCase(Locale.ROOT) != "leo"){
                    Toast.makeText(this@Login,getString(R.string.error_login), Toast.LENGTH_SHORT).show()
                    binding.edtUsuario.error = getString(R.string.error_name)
                }
                else if (binding.edtContra.text.toString().toLowerCase(Locale.ROOT) != "123"){
                    Toast.makeText(this@Login,getString(R.string.error_login), Toast.LENGTH_SHORT).show()
                    binding.edtContra.error = getString(R.string.error_pass)
                }
            }else{
                Toast.makeText(this@Login,getString(R.string.empty_login), Toast.LENGTH_SHORT).show()
            }
        }

        binding.linkSignUp.setOnClickListener{
            val intent = Intent(this@Login, Signup::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}