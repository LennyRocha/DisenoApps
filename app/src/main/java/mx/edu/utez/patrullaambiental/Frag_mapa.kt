package mx.edu.utez.patrullaambiental

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import mx.edu.utez.patrullaambiental.databinding.FragmentFragMapaBinding
import mx.edu.utez.patrullaambiental.model.Reportito
import java.time.LocalDate

/**
 * A simple [Fragment] subclass.
 * Use the [Frag_mapa.newInstance] factory method to
 * create an instance of this fragment.
 */
class Frag_mapa : Fragment() {
    private var _binding: FragmentFragMapaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFragMapaBinding.inflate(inflater, container, false)

        val sharedPreferences = requireActivity().getSharedPreferences("archivo", Context.MODE_PRIVATE)
        val valor = sharedPreferences.getString("usuario","#")
        //binding.textView3.setText(resources.getString(R.string.bienve)+" "+valor)

        val lista = listOf(
            Reportito(R.drawable.baseline_email_24,"Juan",LocalDate.now(),18.8497217, -99.2010277)
        )

        /*
        val adaptador = ContactoAdapter(lista)
        adaptador.onItemClick = { contacto ->
            val intent = Intent(this@MainActivity,DetalleContacto::class.java)
            intent.putExtra("nombre",contacto.nombre)
            Toast.makeText(this@MainActivity,contacto.nombre, Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        binding.rvContactos.adapter = adaptador
        binding.rvContactos.layoutManager =
            LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)

         */

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Evitar memory leaks
    }
}