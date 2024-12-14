package mx.edu.utez.patrullaambiental

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mx.edu.utez.patrullaambiental.adapter.ReporteAdapter
import mx.edu.utez.patrullaambiental.adapter.ReportitoAdapter
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

    fun cargarReportes(){
        val queue = Volley.newRequestQueue(this)

        val url = "http://192.168.230.132:8080/Admin/Rtodos"

        val metodo = Request.Method.GET

        val listener = Response.Listener<JSONObject> { resultado ->
            try {
                val responseRest = resultado.getJSONObject("responseReporte").getJSONArray("reporte")

                val lista = mutableListOf<Reportito>()

                for (i in 0 until responseRest.length()){
                    val jsonReporte = responseRest.getJSONObject(i)

                    val id = jsonReporte.getString("id")
                    val titulo = jsonReporte.getString("titulo")
                    val descripcion = jsonReporte.getString("descripcion")
                    val usuario = jsonReporte.getJSONObject("usuario").getString("nombre")
                    val estado = jsonReporte.getString("estado")
                    val longitud = jsonReporte.getString("longitud")
                    val latitud = jsonReporte.getString("latitud")
                    val imagen = jsonReporte.getString("imagen")

                    val rep = Reportito(id.toInt(),imagen,usuario,titulo,estado,descripcion,longitud.toDouble(),latitud.toDouble())
                    println(usuario)

                    lista.add(rep)
                }

                if(lista.isEmpty()){
                    Toast.makeText(this,getString(R.string.no_reports), Toast.LENGTH_SHORT).show()
                }

                val reportes : List<Reportito> = lista

                val adaptador = ReporteAdapter(reportes)
                adaptador.onItemClick = { repo ->
                    val view = layoutInflater.inflate(R.layout.layout_reportes, null)

                    // Configurar las opciones del menú
                    val btnEdit = view.findViewById<Button>(R.id.btnActualizarRep)
                    val btnDel = view.findViewById<Button>(R.id.btnEliminarRep)

                    btnEdit.setOnClickListener {
                        Toast.makeText(this,"Por terminar....",Toast.LENGTH_SHORT).show()
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
                e.printStackTrace()
            }
        }

        val errorListener = Response.ErrorListener {
                error ->
            Toast.makeText(this, getString(R.string.err_500)+" "+error.message, Toast.LENGTH_SHORT).show()
            println(error.message)
        }

        val request = JsonObjectRequest(metodo,url,null,listener,errorListener)

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