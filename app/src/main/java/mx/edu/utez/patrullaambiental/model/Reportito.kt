package mx.edu.utez.patrullaambiental.model

import java.time.LocalDate

data class Reportito(
    val id : Int,
    val imagen: String,
    val nombre_usuario: String,
    val titulo : String,
    val desc : String,
    val estado : String,
    val longitud: Double,
    val latitud: Double
)