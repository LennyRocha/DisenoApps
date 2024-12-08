package mx.edu.utez.patrullaambiental

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import mx.edu.utez.patrullaambiental.databinding.FragmentFragInicioBinding

class Frag_inicio : Fragment() {
    // Variable para el binding
    private var _binding: FragmentFragInicioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFragInicioBinding.inflate(inflater, container, false)

        val sharedPreferences = requireActivity().getSharedPreferences("archivo", Context.MODE_PRIVATE)
        val valor = sharedPreferences.getString("usuario","#")
        binding.txtBienvenido.setText(resources.getString(R.string.bienve)+" "+valor)

        val animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.entrada_cards)

        val animation2 = AnimationUtils.loadAnimation(requireActivity(), R.anim.entrada_cards_reverse)

        val apareceTexto = AnimationUtils.loadAnimation(requireActivity(), R.anim.entrada_text)

        binding.mainMenu1.startAnimation(animation)
        binding.mainMenu2.startAnimation(animation)
        binding.mainMenu3.startAnimation(animation)
        binding.mainMenu4.startAnimation(animation)
        binding.mainMenu5.startAnimation(animation)
        binding.mainMenu6.startAnimation(animation)
        binding.appbar.startAnimation(animation2)
        binding.txtBienvenido.startAnimation(apareceTexto)

        binding.mainMenu3.setOnClickListener {
            val url = "https://www.utez.edu.mx/wp-content/uploads/2024/09/REGLAMENTO_AMBIENTAL_UTEZ.pdf"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Evitar memory leaks
    }
}

//https://www.utez.edu.mx/boletin-utez-verde/ para después poder descargar boletines o verlos