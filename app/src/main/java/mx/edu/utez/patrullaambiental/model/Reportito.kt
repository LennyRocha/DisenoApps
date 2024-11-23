package mx.edu.utez.patrullaambiental.model

import java.time.LocalDate

data class Reportito(
    val imagen: Int,
    val nombre_usuario: String,
    val fecha: LocalDate,
    val longitud: Double,
    val latitud: Double
)