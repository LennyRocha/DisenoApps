package mx.edu.utez.patrullaambiental

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.edu.utez.patrullaambiental.model.Usuario
import mx.edu.utez.patrullaambiental.databinding.ActivityLoginBinding
import org.json.JSONObject
import java.util.Locale

class Login : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("archivo", Context.MODE_PRIVATE)

        binding.btnAcceder.setOnClickListener{
            if (binding.edtUsuario.text.toString().isNotEmpty() && binding.edtContra.text.toString().isNotEmpty()) {
                obtenerUsuario(binding.edtUsuario.text.toString(), binding.edtContra.text.toString()) { user ->
                    if (user != null) {
                        // Validación dentro del callback
                        if (binding.edtUsuario.text.toString().toLowerCase(Locale.ROOT) == user.email &&
                            binding.edtContra.text.toString() == user.password) {

                            with(sharedPreferences.edit()) {
                                putInt("id", user.Id)
                                putString("usuario", user.nombre)
                                putString("apellido", user.apellido)
                                putString("password", user.password)
                                putString("email", user.email)
                                putString("estado", user.estado)
                                putString("fotoP", user.imagen)
                                commit()
                            }
                            val intent = Intent(this@Login, Inicio::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        } else if (binding.edtUsuario.text.toString().toLowerCase(Locale.ROOT) != user.email) {
                            Toast.makeText(this@Login, getString(R.string.error_login), Toast.LENGTH_SHORT).show()
                            binding.edtUsuario.error = getString(R.string.error_name)
                        } else if (binding.edtContra.text.toString().toLowerCase(Locale.ROOT) != user.password) {
                            Toast.makeText(this@Login, getString(R.string.error_login), Toast.LENGTH_SHORT).show()
                            binding.edtContra.error = getString(R.string.error_pass)
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@Login, getString(R.string.empty_login), Toast.LENGTH_SHORT).show()
            }
        }

        binding.linkSignUp.setOnClickListener{
            val intent = Intent(this@Login, Signup::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    fun obtenerUsuario(correo : String, contra : String,  onResult: (Usuario?) -> Unit) {
        val queue = Volley.newRequestQueue(this)

        val url = "http://192.168.111.81:8080/Admin/Login"

        val metodo = Request.Method.POST

        val body = JSONObject()

        body.put("email", correo)

        body.put("password", contra)

        val listener = Response.Listener<JSONObject> { resultado ->
            try {
                val responseRest = resultado.getJSONObject("responseUsuario").getJSONArray("usuario").getJSONObject(0)
                val name = responseRest.getString("nombre")
                val apellid = responseRest.getString("apellido")
                val pass = responseRest.getString("password")
                val foto = responseRest.getString("fotoP")
                val correito = responseRest.getString("email")
                val estado = responseRest.getString("estado")
                val id = responseRest.getString("id").toInt()

                val usuario = Usuario(
                    id,
                    name,
                    apellid,
                    pass,
                    correito,
                    estado,
                    foto
                )
                onResult(usuario) // Devuelve el usuario al callback
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null) // Devuelve null si algo falla
            }
        }

        val errorListener = Response.ErrorListener {
            error ->
            Toast.makeText(this, getString(R.string.err_500)+" "+error.message, Toast.LENGTH_SHORT).show()
            println(error.message)
        }

        val request = JsonObjectRequest(metodo,url,body,listener,errorListener)

        queue.add(request)
    }
}