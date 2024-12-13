package mx.edu.utez.patrullaambiental

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mx.edu.utez.patrullaambiental.adapter.ReportitoAdapter
import mx.edu.utez.patrullaambiental.databinding.ActivityMostrarReportesBinding
import mx.edu.utez.patrullaambiental.model.Reportito
import org.json.JSONObject
import java.sql.Date

class MostrarReportes : AppCompatActivity() {
    private lateinit var binding: ActivityMostrarReportesBinding
    private lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMostrarReportesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        queue = Volley.newRequestQueue(this)
        val url = "http://192.168.100.80:8080/Admin/Rtodos"
        val metodo = Request.Method.GET
        val resportes = mutableListOf<Reportito>()

        val listener = Response.Listener<JSONObject> { result ->
            val ResponseReporte = result.getJSONObject("responseReporte")
            val arreglo = ResponseReporte.getJSONArray("reporte")
            for (i in 0 until arreglo.length()) {
                resportes.add(
                    Reportito(
                        arreglo.getJSONObject(i).getInt("id"),
                        arreglo.getJSONObject(i).getString("nombre_usuario"),
                        Date.valueOf(arreglo.getJSONObject(i).getString("fecha")),
                        arreglo.getJSONObject(i).getDouble("longitud"),
                        arreglo.getJSONObject(i).getDouble("latitud")
                    )
                )
            }

            val adaptador =ReportitoAdapter(resportes)

            binding.rVmostrarReporte.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false )
            binding.rVmostrarReporte.adapter = adaptador
        }

        val errorListener = Response.ErrorListener { error ->
            Log.e("ERROR VOLLEY", error.message.toString())
        }

        val request = JsonObjectRequest(metodo, url, null, listener, errorListener)
        queue.add(request)

    }
}