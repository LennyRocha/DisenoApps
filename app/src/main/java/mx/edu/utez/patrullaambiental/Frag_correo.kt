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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mx.edu.utez.patrullaambiental.adapter.ReportitoAdapter
import mx.edu.utez.patrullaambiental.databinding.FragmentFragCorreoBinding
import mx.edu.utez.patrullaambiental.databinding.FragmentFragMapaBinding
import mx.edu.utez.patrullaambiental.model.Reportito
import java.time.LocalDate

/**
 * A simple [Fragment] subclass.
 * Use the [Frag_correo.newInstance] factory method to
 * create an instance of this fragment.
 */

class Frag_correo : Fragment() {
    private var _binding: FragmentFragCorreoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFragCorreoBinding.inflate(inflater, container, false)

        val sharedPreferences = requireActivity().getSharedPreferences("archivo", Context.MODE_PRIVATE)
        val valor = sharedPreferences.getString("usuario", "#")

        Toast.makeText(requireActivity(),"Por construir",Toast.LENGTH_SHORT).show()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Evitar memory leaks
    }
}