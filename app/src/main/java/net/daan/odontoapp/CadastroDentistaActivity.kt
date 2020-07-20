package net.daan.odontoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_cadastro_dentista.*
import net.daan.odontoapp.http.HttpHelper
import net.daan.odontoapp.model.Dentista
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import kotlin.math.log

class CadastroDentistaActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var senha: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_dentista)

        init()
    }

    private fun init() {
        email = intent.getStringExtra("EMAIL")!!
        senha = intent.getStringExtra("SENHA")!!

        btnCadastrar.setOnClickListener {
            val dentista = Dentista()
            dentista.nome = txtNome.text.toString()
            dentista.cro = txtCro.text.toString()
            dentista.email = txtEmail.text.toString()
            dentista.telefone = txtTelefone.text.toString()

            val gson = Gson()
            val dentistaJson = gson.toJson(dentista)

            doAsync {
                val httpHelper = HttpHelper(email, senha)
                val response = httpHelper.post("/dentistas", dentistaJson)

                uiThread {
                    val abrirCadastrarFoto = Intent(this@CadastroDentistaActivity, CadastroFotoActivity::class.java)

                    abrirCadastrarFoto.putExtra("DENTISTA", response)
                    abrirCadastrarFoto.putExtra("EMAIL", email)
                    abrirCadastrarFoto.putExtra("SENHA", senha)

                    startActivity(abrirCadastrarFoto)
                }
            }
        }
    }
}
