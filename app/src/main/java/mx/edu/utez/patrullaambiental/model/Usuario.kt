package mx.edu.utez.patrullaambiental.model

data class Usuario(
    val Id: Int,
    val nombre: String,
    val apellido: String,
    val password: String,
    val email: String,
    val estado: String,
    val imagen : String
)