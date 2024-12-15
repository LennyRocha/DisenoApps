package mx.edu.utez.patrullaambiental.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mx.edu.utez.patrullaambiental.R
import mx.edu.utez.patrullaambiental.model.Reportito

class ReporteAdapter(private val reportes: List<Reportito>) :
    RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder>() {

    var onItemClick: ((Reportito) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_reportes, parent, false)
        return ReporteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReporteViewHolder, position: Int) {
        val reporte = reportes[position]
        holder.bind(reporte)

    }

    override fun getItemCount(): Int = reportes.size

    inner class ReporteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titulo: TextView = itemView.findViewById(R.id.txtTitRepLay)
        private val descripcion: TextView = itemView.findViewById(R.id.txtDescRepLay)
        private val usuario: TextView = itemView.findViewById(R.id.txtDescRepLay)
        private val imagen: ImageView = itemView.findViewById(R.id.imgReporte)

        fun bind(reporte: Reportito) {
            titulo.text = reporte.titulo
            descripcion.text = reporte.desc
            usuario.text = reporte.nombre_usuario
            Glide.with(itemView.context)
                .load(reporte.imagen) // Asegúrate de manejar imágenes Base64 si es necesario
                .into(imagen)

            itemView.setOnClickListener {
                onItemClick?.invoke(reporte)
            }
        }
    }
}
