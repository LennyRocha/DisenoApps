package mx.edu.utez.patrullaambiental

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mx.edu.utez.patrullaambiental.databinding.FragmentInicioBinding

class Frag_inicio : Fragment() {
    // Variable para el binding
    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)

        val sharedPreferences = requireActivity().getSharedPreferences("archivo", Context.MODE_PRIVATE)
        val valor = sharedPreferences.getString("usuario","#")
        binding.txtBienvenido.setText(resources.getString(R.string.bienve)+" "+valor)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Evitar memory leaks
    }
}