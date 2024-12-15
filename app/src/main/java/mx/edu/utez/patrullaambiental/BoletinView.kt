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
import mx.edu.utez.patrullaambiental.databinding.ActivityBoletinViewBinding
import mx.edu.utez.patrullaambiental.databinding.ActivityDocumentViewBinding

class BoletinView : AppCompatActivity() {
    private lateinit var binding: ActivityBoletinViewBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoletinViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val webView: WebView = binding.webView

        if (isInternetAvailable()) {
            // Configuración del WebView para cargar el PDF
            webView.webViewClient = WebViewClient()
            webView.settings.javaScriptEnabled = true
            val pdfUrl = "https://www.utez.edu.mx/wp-content/uploads/2024/09/Boletin_UTEZ_Verde_No4.pdf"
            webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$pdfUrl")
        } else {
            // Mostrar Snackbar si no hay Internet
            Snackbar.make(webView, getString(R.string.noInternet), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.reintenta)) {
                    recreate()
                }
                .show()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Documento cerrado")
        Toast.makeText(this, "PDF cerrado", Toast.LENGTH_SHORT).show()
    }
}