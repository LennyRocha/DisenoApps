package mx.edu.utez.patrullaambiental

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import mx.edu.utez.patrullaambiental.databinding.FragmentFragMapaBinding
import mx.edu.utez.patrullaambiental.databinding.FragmentFragNoInternetBinding

/**
 * A simple [Fragment] subclass.
 * Use the [Frag_no_internet.newInstance] factory method to
 * create an instance of this fragment.
 */
class Frag_no_internet : Fragment() {

    private var _binding: FragmentFragNoInternetBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressBar: ProgressBar
    private lateinit var btnRetry: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFragNoInternetBinding.inflate(inflater, container, false)

        progressBar = binding.pBar
        btnRetry = binding.btnRetryConnect
        Glide.with(this)
            .asGif()
            .load(R.drawable.no_internet_bot)
            .into(binding.imgNoConecct)

        btnRetry.setOnClickListener {
            retryConnection()
        }

        return binding.root
    }

    private fun retryConnection() {
        progressBar.visibility = View.VISIBLE
        btnRetry.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            if (isInternetAvailable(requireContext())) {
                // Si hay conexión, carga el fragmento original
                (activity as Inicio).loadFragment(Frag_noticias())
            } else {
                progressBar.visibility = View.GONE
                btnRetry.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    "No hay conexión a Internet. Inténtalo de nuevo.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, 3000) // Simula un retraso de 3 segundos para verificar la conexión
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
