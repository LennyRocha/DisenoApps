package mx.edu.utez.patrullaambiental.model

data class Reportito(
    val id: Int,
    val imagen: String,
    val nombre_usuario: String,
    val titulo: String,
    val desc: String,
    val estado: String,
    val longitud: Float,
    val latitud: Float
)