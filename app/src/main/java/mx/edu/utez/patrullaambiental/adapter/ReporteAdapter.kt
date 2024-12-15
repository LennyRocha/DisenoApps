package mx.edu.utez.patrullaambiental.adapter

import android.graphics.BitmapFactory
import android.util.Base64
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
        private val usuario: TextView = itemView.findViewById(R.id.txtNombreRepLay)
        private val imagen: ImageView = itemView.findViewById(R.id.imgReporte)

        fun bind(reporte: Reportito) {
            titulo.text = reporte.titulo
            descripcion.text = reporte.desc
            usuario.text = reporte.nombre_usuario

            // Decodificar imagen Base64 y cargar con Glide
            val base64String = reporte.imagen
            if (base64String.isNotEmpty()) {
                try {
                    val decodedString = Base64.decode(base64String, Base64.DEFAULT)
                    val decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                    Glide.with(itemView.context)
                        .load(decodedBitmap)
                        .placeholder(R.drawable.placeholder) // Imagen mientras se carga
                        .error(R.drawable.error_placeholder) // Imagen si ocurre un error
                        .into(imagen)
                } catch (e: Exception) {
                    imagen.setImageResource(R.drawable.error_placeholder) // Imagen de respaldo si falla
                }
            } else {
                imagen.setImageResource(R.drawable.error_placeholder) // Imagen de respaldo si no hay Base64
            }

            itemView.setOnClickListener {
                onItemClick?.invoke(reporte)
            }
        }
    }
}
