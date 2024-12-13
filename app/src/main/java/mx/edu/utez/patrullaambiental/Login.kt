package mx.edu.utez.patrullaambiental

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import mx.edu.utez.patrullaambiental.databinding.ActivityLoginBinding
import mx.edu.utez.patrullaambiental.model.Usuario
import org.json.JSONObject

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var queue: RequestQueue
    private val usuarios = mutableListOf<Usuario>() 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        queue = Volley.newRequestQueue(this)

        binding.btnAcceder.setOnClickListener {
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
                    usuarios.clear()
                    for (i in 0 until arreglo.length()) {
                        val usuario = Usuario(
                            arreglo.getJSONObject(i).getInt("id"),
                            arreglo.getJSONObject(i).getString("nombre"),
                            arreglo.getJSONObject(i).getString("apellido"),
                            arreglo.getJSONObject(i).getString("password"),
                            arreglo.getJSONObject(i).getString("email"),
                            arreglo.getJSONObject(i).getString("tipo"),
                            arreglo.getJSONObject(i).getString("estado")
                        )
                        usuarios.add(usuario)
                    }

                    val usuarioEncontrado = usuarios.find {
                        it.nombre.equals(nombre, ignoreCase = true) && it.password == contra
                    }

                    if (usuarioEncontrado != null) {
                        Snackbar.make(binding.root, "Bienvenido, ${usuarioEncontrado.nombre}", Snackbar.LENGTH_LONG).show()

                        val intent = Intent(this@Login, Inicio::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }

                val errorListener = Response.ErrorListener { error ->
                    Log.e("Error al obtener usuarios", error.message.toString())
                }

                val request = JsonObjectRequest(metodo, url, null, listener, errorListener)
                queue.add(request)

            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.linkSignUp.setOnClickListener {
            val intent = Intent(this@Login, Signup::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}