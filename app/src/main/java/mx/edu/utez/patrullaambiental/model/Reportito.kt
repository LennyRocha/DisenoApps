package mx.edu.utez.patrullaambiental.model

import java.sql.Date

data class Reportito(
    val imagen: Int,
    val nombre_usuario: String,
    val fecha: Date,
    val longitud: Double,
    val latitud: Double
)