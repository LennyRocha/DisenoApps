package mx.edu.utez.patrullaambiental

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.google.android.material.snackbar.Snackbar
import mx.edu.utez.patrullaambiental.adapter.Usuario
import mx.edu.utez.patrullaambiental.databinding.ActivityLoginBinding
import org.json.JSONObject
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

    fun obtenerUsuario() {
        Log.d("Login", "Botón clickeado")
        val nombre = binding.edtUsuario.text.toString()
        val contra = binding.edtContra.text.toString()

        if (nombre.isNotEmpty() && contra.isNotEmpty()) {
            val url = "http://192.168.0.166:8080/Admin/Utodos"
            val metodo = Request.Method.GET

            val listener = Response.Listener<JSONObject> { response ->
                Log.d("Listener", "Se realiza la petición")
                val responseUsuario = response.getJSONObject("responseUsuario")
                val arreglo = responseUsuario.getJSONArray("usuario")
                val usuarios = mutableListOf<Usuario>()
                usuarios.clear()
                for (i in 0 until arreglo.length()) {
                    val usuario = Usuario(
                        arreglo.getJSONObject(i).getInt("id"),
                        arreglo.getJSONObject(i).getString("nombre"),
                        arreglo.getJSONObject(i).getString("apellido"),
                        arreglo.getJSONObject(i).getString("password"),
                        arreglo.getJSONObject(i).getString("email"),
                        arreglo.getJSONObject(i).getString("estado"),
                        arreglo.getJSONObject(i).getString("fotoP"),
                    )
                    usuarios.add(usuario)

                    //val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)
                }

                val usuarioEncontrado = usuarios.find {
                    it.nombre.equals(nombre, ignoreCase = true) && it.password == contra
                }

                if (usuarioEncontrado != null) {
                    Snackbar.make(
                        binding.root,
                        "Bienvenido, ${usuarioEncontrado.nombre}",
                        Snackbar.LENGTH_LONG
                    ).show()

                    val intent = Intent(this@Login, Inicio::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}