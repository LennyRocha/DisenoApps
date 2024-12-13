package mx.edu.utez.patrullaambiental.model

import java.time.LocalDate

data class Usuario(
    val Id: Int,
    val nombre: String,
    val apellido: String,
    val password: String,
    val email: String,
    val tipo : String,
    val estado:String
)