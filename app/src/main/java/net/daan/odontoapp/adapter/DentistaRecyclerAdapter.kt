package net.daan.odontoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_cadastro_foto.view.*
import kotlinx.android.synthetic.main.layout_lista_dentistas.view.*
import net.daan.odontoapp.R
import net.daan.odontoapp.model.Dentista

@Suppress("UselessCallOnNotNull")
class DentistaRecyclerAdapter(var list: List<Dentista>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_lista_dentistas, parent, false)

        return DentistaViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DentistaViewHolder -> {
                holder.bind(list[position])
            }
        }
    }

    class DentistaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtNome: TextView = itemView.txtNome
        val txtEmail: TextView = itemView.txtEmail
        val txtCro: TextView = itemView.txtCro
        val txtTelefone: TextView = itemView.txtTelefone
        val imgDentista: ImageView = itemView.imgDentista

        fun bind(dentista: Dentista) {
            txtNome.text = dentista.nome
            txtEmail.text = dentista.email
            txtCro.text = dentista.cro
            txtTelefone.text = dentista.telefone

            if (!dentista.urlImagem.isNullOrEmpty()) {
                val requestOptions = RequestOptions()
                    .placeholder(R.drawable.ic_person_light)
                    .error(R.drawable.ic_sentiment)

                Glide.with(itemView.context)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(dentista.urlImagem)
                    .into(imgDentista)
            } else {
                imgDentista.setImageResource(R.drawable.ic_person_light)
            }
        }
    }
}