package mx.edu.utez.patrullaambiental

import android.content.Context
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Evitar memory leaks
    }
}