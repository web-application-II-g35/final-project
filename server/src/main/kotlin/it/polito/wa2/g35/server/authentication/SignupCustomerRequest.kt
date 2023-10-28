package it.polito.wa2.g35.server.authentication

data class SignupCustomerRequest (
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val contact: String,
    val address1: String,
    val address2: String?
)
