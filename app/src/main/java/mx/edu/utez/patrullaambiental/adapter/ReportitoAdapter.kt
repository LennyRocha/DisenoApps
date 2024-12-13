package mx.edu.utez.patrullaambiental.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.edu.utez.patrullaambiental.databinding.LayoutReportesMapaBinding
import mx.edu.utez.patrullaambiental.model.Reportito
import mx.edu.utez.patrullaambiental.utils.loadBase64Image

class ReportitoAdapter (var lista : List<Reportito>) :
    RecyclerView.Adapter<ReportitoAdapter.ViewHolder>() {

    var onItemClick: ((Reportito) -> Unit)? = null

    class ViewHolder(val binding: LayoutReportesMapaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Crear la vista, infla el xml del layout_contacto
        return ViewHolder(
            LayoutReportesMapaBinding.inflate(
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
            txtUsuario.text = reportito.nombre_usuario
            txtFecha.text = reportito.titulo
            txtLong.text = reportito.longitud.toString()
            txtLat.text = reportito.latitud.toString()
            txtIdRep.text = reportito.id.toString()
            txtDescripcionRep.text = reportito.desc
            loadBase64Image(reportito.imagen,imgEvidencia)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(reportito)
        }
    }
}