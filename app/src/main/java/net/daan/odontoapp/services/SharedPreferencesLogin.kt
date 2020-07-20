package net.daan.odontoapp.services

import android.content.Context
import net.daan.odontoapp.model.SharedPreferencesUser

class SharedPreferencesLogin(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun setLoginData(email: String, senha: String, saveCredentials: Boolean) {
        editor.putString("email", email)
        editor.putString("senha", senha)
        editor.putBoolean("saveCredentials", saveCredentials)

        editor.commit()
    }

    fun getLoginData(): SharedPreferencesUser {
        val email = sharedPreferences.getString("email", "")
        val senha = sharedPreferences.getString("senha", "")
        val saveCredentials = sharedPreferences.getBoolean("saveCredentials", false)

        val sharedPreferencesUser = SharedPreferencesUser()
        sharedPreferencesUser.email = email!!
        sharedPreferencesUser.senha = senha!!
        sharedPreferencesUser.saveCredentials = saveCredentials!!

        return sharedPreferencesUser
    }

    fun removeLoginData() {
        editor.putString("email", "")
        editor.putString("senha", "")
        editor.putBoolean("saveCredentials", false)

        editor.commit()
    }
}