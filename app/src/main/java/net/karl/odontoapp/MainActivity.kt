package net.karl.odontoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import net.karl.odontoapp.adapter.DentistaRecyclerAdapter
import net.karl.odontoapp.http.HttpHelper
import net.karl.odontoapp.model.Dentista
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var dentistaRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var dentistas: List<Dentista>

    private lateinit var email: String
    private lateinit var senha: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        email = intent.getStringExtra("EMAIL")!!
        senha = intent.getStringExtra("SENHA")!!

        doAsync {
            updateList()

            uiThread {
                dentistaRecyclerAdapter = DentistaRecyclerAdapter(dentistas)
                rvDentistas.layoutManager = LinearLayoutManager(this@MainActivity)
                rvDentistas.adapter = dentistaRecyclerAdapter
            }
        }

        btnRefresh.setOnClickListener {
            doAsync {
                updateList()

                uiThread {
                    rvDentistas.swapAdapter(DentistaRecyclerAdapter(dentistas), false)
                    dentistaRecyclerAdapter.notifyDataSetChanged()

                    Toast.makeText(this@MainActivity, "Updated", Toast.LENGTH_LONG).show()
                }
            }
        }

        btnAbrirCadastrarDentista.setOnClickListener {
            val abrirCadastroDentista = Intent(this, CadastroDentistaActivity::class.java)

            abrirCadastroDentista.putExtra("EMAIL", email)
            abrirCadastroDentista.putExtra("SENHA", senha)

            startActivity(abrirCadastroDentista)
        }
    }

    private fun updateList() {
        val gson = Gson()
        val httpHelper = HttpHelper(email, senha)

        val response = httpHelper.get("/dentistas")

        dentistas = gson.fromJson(response, Array<Dentista>::class.java).toList()
    }
}
