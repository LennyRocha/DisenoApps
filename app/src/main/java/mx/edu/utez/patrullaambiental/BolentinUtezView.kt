package mx.edu.utez.patrullaambiental

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import mx.edu.utez.patrullaambiental.databinding.ActivityBolentinUtezViewBinding

class BolentinUtezView : AppCompatActivity() {
    private lateinit var binding: ActivityBolentinUtezViewBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBolentinUtezViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val webView: WebView = binding.WvBoletin

        if (isInternetAvailable()){
            webView.webViewClient = WebViewClient()
            webView.settings.javaScriptEnabled = true
            webView.loadUrl("https://www.utez.edu.mx/boletin-utez-verde/")
        } else{
            Snackbar.make(webView, getString(R.string.noInternet) , Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.reintenta)){
                    recreate()
                }
                .show()
        }

    }

    private fun isInternetAvailable(): Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Pagina cerrada")
        Toast.makeText(this, "Pagina Cerrada", Toast.LENGTH_SHORT).show()

    }
}