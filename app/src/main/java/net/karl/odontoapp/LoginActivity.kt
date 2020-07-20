package net.karl.odontoapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import net.karl.odontoapp.http.HttpHelper
import net.karl.odontoapp.services.SharedPreferencesLogin
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferencesLogin = SharedPreferencesLogin(this)
        val sharedPreferencesUser = sharedPreferencesLogin.getLoginData()

        if (sharedPreferencesUser.saveCredentials) {
            edit_text_email.text = SpannableStringBuilder(sharedPreferencesUser.email)
            edit_text_senha.text = SpannableStringBuilder(sharedPreferencesUser.senha)
            check_box_mantenha.isChecked = true
        }

        button_entrar.setOnClickListener {
            buttonEntrarPressed()
        }
    }

    private fun buttonEntrarPressed() {
        val email = edit_text_email.text.toString()
        val senha = edit_text_senha.text.toString()

        doAsync {
            val httpHelper = HttpHelper(email, senha)
            httpHelper.setURL("http://192.168.0.105:8080")

            val response = httpHelper.get("/login")

            if (response.isEmpty()) {
                uiThread {
                    text_view_invalido.visibility = View.VISIBLE
                }
            } else if (response.contains("403")) {
                uiThread {
                    val mainActivity = Intent(this@LoginActivity, MainActivity::class.java)

                    mainActivity.putExtra("EMAIL", email)
                    mainActivity.putExtra("SENHA", senha)

                    if (check_box_mantenha.isChecked) {
                        sharedPreferencesLogin.setLoginData(email, senha, true)
                    } else {
                        sharedPreferencesLogin.removeLoginData()
                    }

                    startActivity(mainActivity)
                }
            }
        }
    }
}
