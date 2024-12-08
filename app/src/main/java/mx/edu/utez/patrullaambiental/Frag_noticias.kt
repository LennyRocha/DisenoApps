package mx.edu.utez.patrullaambiental

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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