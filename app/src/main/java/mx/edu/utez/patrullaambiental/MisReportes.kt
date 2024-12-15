package mx.edu.utez.patrullaambiental

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.edu.utez.patrullaambiental.adapter.ReporteAdapter
import mx.edu.utez.patrullaambiental.databinding.ActivityMisReportesBinding
import mx.edu.utez.patrullaambiental.model.Reportito
import org.json.JSONObject

class MisReportes : AppCompatActivity() {
    private lateinit var binding : ActivityMisReportesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisReportesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarReports)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
            //title = getString(R.string.miPer)
        }

        val animation = AnimationUtils.loadAnimation(this, R.anim.entrada_cards)

        binding.rvHistReps.startAnimation(animation)

        cargarReportes()
    }

    fun cargarReportes() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.0.13:8080/Admin/Rtodos"
        val metodo = Request.Method.GET

        val listener = Response.Listener<JSONObject> { resultado ->
            try {
                // Obtén el array de reportes desde el JSON
                val responseRest = resultado.getJSONObject("responseReporte").getJSONArray("reporte")

                val lista = mutableListOf<Reportito>()

                // Recorre cada reporte en el array
                for (i in 0 until responseRest.length()) {
                    val jsonReporte = responseRest.getJSONObject(i)

                    // Obtén los valores de forma segura
                    val id = jsonReporte.optInt("id", -1) // Valor predeterminado en caso de error
                    val titulo = jsonReporte.optString("titulo", "Sin título")
                    val descripcion = jsonReporte.optString("descripcion", "Sin descripción")
                    val usuario = jsonReporte.optJSONObject("usuario")?.optString("nombre", "Anónimo") ?: "Anónimo"
                    val estado = jsonReporte.optString("estado", "Desconocido")
                    val longitud = jsonReporte.optString("longitud", "0.0").toFloatOrNull() ?: 0.0f
                    val latitud = jsonReporte.optString("latitud", "0.0").toFloatOrNull() ?: 0.0f
                    val imagen = jsonReporte.optString("imagen", "")

                    // Asegúrate de agregar solo reportes válidos
                    if (id != -1) {
                        val rep = Reportito(id, imagen, usuario, titulo, estado, descripcion, longitud, latitud)
                        lista.add(rep)
                    }
                }

                // Si no hay reportes en la lista
                if (lista.isEmpty()) {
                    Toast.makeText(this, getString(R.string.no_reports), Toast.LENGTH_SHORT).show()
                }

                // Configura el RecyclerView con la lista de reportes
                val reportes: List<Reportito> = lista
                val adaptador = ReporteAdapter(reportes)

                adaptador.onItemClick = { repo ->
                    val view = layoutInflater.inflate(R.layout.layout_reportes, null)

                    // Configurar las opciones del menú
                    val btnEdit = view.findViewById<Button>(R.id.btnActualizarRep)
                    val btnDel = view.findViewById<Button>(R.id.btnEliminarRep)

                    btnEdit.setOnClickListener {
                        Toast.makeText(this, "Por terminar....", Toast.LENGTH_SHORT).show()
                    }

                    btnDel.setOnClickListener {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle(getString(R.string.del_rep))
                        builder.setIcon(R.drawable.logo_verde)
                        builder.setPositiveButton(getString(R.string.si)) { dialog, _ ->
                            deleteReport(repo.id)
                        }
                        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                            dialog.cancel()
                        }
                        builder.show()
                    }
                }

                binding.rvHistReps.adapter = adaptador
                binding.rvHistReps.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            } catch (e: Exception) {
                Log.e("MisReportes", "Error procesando el JSON", e)
                Toast.makeText(this, "Error al cargar los reportes", Toast.LENGTH_SHORT).show()
            }
        }

        val errorListener = Response.ErrorListener { error ->
            Log.e("MisReportes", "Error al obtener los reportes", error)
            Toast.makeText(this, getString(R.string.err_500) + " " + error.message, Toast.LENGTH_SHORT).show()
        }

        val request = JsonObjectRequest(metodo, url, null, listener, errorListener)

        queue.add(request)
    }



    fun deleteReport (id : Int){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.1.67:8080/Admin/UEliminar/${id}"
        val metodo = Request.Method.DELETE
        val body  = null

        val listener = Response.Listener<JSONObject> { resulttado ->
            Log.d("Insercion", "REPORTE ELIMINADO")
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            Toast.makeText(this,getString(R.string.del_exito),Toast.LENGTH_SHORT).show()
        }

        val errorListener = Response.ErrorListener { error -> Toast.makeText(this, getString(R.string.del_fallo)+" "+error.message, Toast.LENGTH_SHORT).show() }
        val request = JsonObjectRequest(metodo, url, body, listener, errorListener)
        queue.add(request)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Manejar la acción de la flecha aquí
                val intent = Intent(this@MisReportes,Inicio::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}