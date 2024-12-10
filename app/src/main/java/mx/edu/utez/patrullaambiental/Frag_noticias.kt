package mx.edu.utez.patrullaambiental

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import mx.edu.utez.patrullaambiental.databinding.FragmentFragNoticiasBinding

/**
 * A simple [Fragment] subclass.
 * Use the [Frag_noticias.newInstance] factory method to
 * create an instance of this fragment.
 */

class Frag_noticias : Fragment() {
    private var _binding: FragmentFragNoticiasBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFragNoticiasBinding.inflate(inflater, container, false)

        //de aqui
        val webView: WebView = binding.WvNoticias
        setupWebView(webView)
        //aqui
        return binding.root
    }

    private fun setupWebView(webView: WebView){
        val webView: WebView = binding.WvNoticias
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://www.facebook.com/PatrullaUTEZ/about")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    
}