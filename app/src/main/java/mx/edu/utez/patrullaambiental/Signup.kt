package mx.edu.utez.patrullaambiental

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import mx.edu.utez.patrullaambiental.databinding.ActivitySignupBinding
import org.json.JSONObject

class Signup : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    private lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        queue = Volley.newRequestQueue(this)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val typeface = ResourcesCompat.getFont(this, R.font.sour_gummy_extra_bold)
        binding.btnRegistrame.setOnClickListener{
            val nombre = binding.edtNewNombre.text.toString()
            val apellido = binding.edtNewApellido.text.toString()
            val correo = binding.edtNewCorreo.text.toString()
            val contrasena= binding.edtNewContra.text.toString()
            val confirmarContra= binding.edtConfirContra.text.toString()
            val estado = "Activo"
            if(nombre.isNotEmpty()&&apellido.isNotEmpty()&&correo.isNotEmpty()&&contrasena.isNotEmpty()&&confirmarContra.isNotEmpty()){
                if(contrasena.equals(confirmarContra)){
                    val url = "http://192.168.0.166:8080/Admin/UCrear"
                    val metodo = Request.Method.POST
                    val body = JSONObject()
                    body.put("nombre", nombre)
                    body.put("apellido", apellido)
                    body.put("password", contrasena)
                    body.put("email", correo)
                    body.put("estado", estado)
                    val listener = Response.Listener<JSONObject> {response ->
                        Log.d("Insercion", "USUARIO INSERTADO")
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Login::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    val errorListener = Response.ErrorListener { error->
                        Log.e("ERROR AL INSERTAR EL USUARIO", error.message.toString())
                    }
                    val request= JsonObjectRequest(metodo,url,body,listener,errorListener)
                    queue.add(request)
                }else{
                    Toast.makeText(this,"Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this,"Llena todos los campos", Toast.LENGTH_SHORT).show()
            }

        }


        binding.uploadIcon.setOnClickListener{
            val snack =
                Snackbar.make(binding.main, "Funcionalidad en construcción...", Snackbar.LENGTH_SHORT)
            snack.setAction("Aceptar") {
                snack.dismiss()
            }
            snack.show()
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
            //title = getString(R.string.inicioReg)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Manejar la acción de la flecha aquí
                val intent = Intent(this@Signup, Login::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}