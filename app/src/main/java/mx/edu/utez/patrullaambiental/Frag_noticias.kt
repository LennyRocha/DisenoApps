package mx.edu.utez.patrullaambiental

import android.annotation.SuppressLint
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

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFragNoticiasBinding.inflate(inflater, container, false)

        val webView: WebView = binding.vistaFace
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://www.facebook.com/PatrullaUTEZ/about")

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Evitar memory leaks
    }
}