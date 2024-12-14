package mx.edu.utez.patrullaambiental

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mx.edu.utez.patrullaambiental.adapter.ReportitoAdapter
import mx.edu.utez.patrullaambiental.databinding.FragmentFragMapaBinding
import mx.edu.utez.patrullaambiental.model.Reportito
import org.json.JSONObject
import java.time.LocalDate

/**
 * A simple [Fragment] subclass.
 * Use the [Frag_mapa.newInstance] factory method to
 * create an instance of this fragment.
 */
class Frag_mapa : Fragment(), LocationListener {
    private var _binding: FragmentFragMapaBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationManager: LocationManager
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFragMapaBinding.inflate(inflater, container, false)

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                10
            )
        } else {
            setupMap()
            cargarReportes()
        }

        binding.rvReportees.visibility = View.INVISIBLE

        binding.btnMostrar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))

        binding.btnMostrar.setOnClickListener {

            binding.btnMostrar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            if (binding.rvReportees.visibility == View.VISIBLE) {
                binding.rvReportees.animate()
                    .alpha(0f)
                    .setDuration(500)
                    .withEndAction {
                        binding.rvReportees.visibility = View.GONE
                        binding.btnMostrar.setImageResource(android.R.drawable.ic_input_add)
                        binding.btnMostrar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                        googleMap.clear()
                    }
            } else {
                binding.rvReportees.alpha = 0f
                binding.rvReportees.visibility = View.VISIBLE
                binding.rvReportees.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .withEndAction {
                        binding.btnMostrar.setImageResource(android.R.drawable.ic_delete)
                        binding.btnMostrar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                    }
            }
        }

        return binding.root
    }

    private fun setupMap() {
        val contendorMapa = childFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        contendorMapa.getMapAsync { map ->
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                map.isMyLocationEnabled = true
            }
            googleMap = map
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Evitar memory leaks
    }

    override fun onLocationChanged(p0: Location) {
        Log.e("UBICACION", "LONGITUD: ${p0.longitude}, ALTITUD: ${p0.altitude} LATITUD: ${p0.latitude}")
    }

    fun cargarReportes(){
        val queue = Volley.newRequestQueue(requireActivity())

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
                    println(id)

                    lista.add(rep)
                }

                if(lista.isEmpty()){
                    Toast.makeText(requireActivity(),getString(R.string.no_reports),Toast.LENGTH_SHORT).show()
                }else {
                    Log.d("RecyclerView", "Lista cargada con ${lista.size} elementos.")
                }

                val reportes : List<Reportito> = lista

                val adaptador = ReportitoAdapter(reportes)
                adaptador.onItemClick = { repo ->
                    val latLng = LatLng(repo.latitud, repo.longitud)
                    googleMap.addMarker(MarkerOptions().position(latLng).title(repo.nombre_usuario))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                    Toast.makeText(requireActivity(), "Hola "+repo.nombre_usuario, Toast.LENGTH_SHORT).show()
                }

                binding.rvReportees.adapter = adaptador
                binding.rvReportees.layoutManager =
                    LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val errorListener = Response.ErrorListener {
                error ->
            Toast.makeText(requireActivity(), getString(R.string.err_500)+" "+error.message, Toast.LENGTH_SHORT).show()
            println(error.message)
        }

        val request = JsonObjectRequest(metodo,url,null,listener,errorListener)

        queue.add(request)
    }
}