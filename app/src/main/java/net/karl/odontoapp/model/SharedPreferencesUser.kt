package net.karl.odontoapp.model

class SharedPreferencesUser {
    var email = ""
    var senha = ""
    var saveCredentials = false

    override fun toString(): String {
        return "SharedPreferencesUser(email='$email', senha='$senha', saveCredentials=$saveCredentials)"
    }
}