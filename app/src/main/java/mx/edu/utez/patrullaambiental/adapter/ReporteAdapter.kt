package mx.edu.utez.patrullaambiental.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.edu.utez.patrullaambiental.databinding.LayoutReportesBinding
import mx.edu.utez.patrullaambiental.databinding.LayoutReportesMapaBinding
import mx.edu.utez.patrullaambiental.model.Reportito
import mx.edu.utez.patrullaambiental.utils.loadBase64Image

class ReporteAdapter (var lista : List<Reportito>) :
    RecyclerView.Adapter<ReporteAdapter.ViewHolder>() {

    var onItemClick: ((Reportito) -> Unit)? = null

    class ViewHolder(val binding: LayoutReportesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Crear la vista, infla el xml del layout_contacto
        return ReporteAdapter.ViewHolder(
            LayoutReportesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        //Devuelve la cantidad de elementos de la lista
        return lista.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Llena la vista, con los datos de la lista
        val reportito = lista[position]
        with(holder.binding) {
            txtNombreRepLay.text = reportito.nombre_usuario
            txtTitRepLay.text = reportito.titulo
            txtLongRepLay.text = reportito.longitud.toString()
            txtLatRepLay.text = reportito.latitud.toString()
            txtIdRepLay.text = reportito.id.toString()
            txtNombreRepLay.text = reportito.desc
            loadBase64Image(reportito.imagen,imgReporte)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(reportito)
        }
    }
}