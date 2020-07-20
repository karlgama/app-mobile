package net.karl.odontoapp

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_cadastro_foto.*
import net.karl.odontoapp.http.HttpHelper
import net.karl.odontoapp.model.Dentista
import net.karl.odontoapp.model.FileUpload
import net.karl.odontoapp.model.FileUploaded
import net.karl.odontoapp.utils.HashUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class CadastroFotoActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var senha: String

    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_foto)

        init()
    }

    private fun init() {
        email = intent.getStringExtra("EMAIL")!!
        senha = intent.getStringExtra("SENHA")!!

        fbImage.setOnClickListener {
            openGallery()
        }

        btnCadastrar.setOnClickListener {
            val fileUpload = FileUpload()
            fileUpload.fileName = HashUtils.genereateUniqueHash() + ".png"
            fileUpload.mimeType = "image/png"
            fileUpload.base64 = HashUtils.generateBase64FromImageView(imgDentistaPreview)

            val dentista = gson.fromJson(intent.getStringExtra("DENTISTA"), Dentista::class.java)

            doAsync {
                val fileUploaded = uploadFile(fileUpload)
                addImageToDentista(dentista, fileUploaded.url)

                uiThread {
                    val mainActivity = Intent(this@CadastroFotoActivity, MainActivity::class.java)

                    mainActivity.putExtra("EMAIL", email)
                    mainActivity.putExtra("SENHA", senha)

                    startActivity(mainActivity)
                }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Selecionar Imagem"), 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1000 && resultCode == -1 && data != null) {
            imgDentistaPreview.setImageURI(data.data)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun uploadFile(fileUpload: FileUpload): FileUploaded {
        val http = HttpHelper(email, senha)

        val fileUploadObject = gson.toJson(fileUpload)
        val responseFileUpload = http.post("/dentistas/upload", fileUploadObject)

        return gson.fromJson(responseFileUpload, FileUploaded::class.java)
    }

    fun addImageToDentista(dentista: Dentista, url: String): Dentista {
        val http = HttpHelper(email, senha)

        dentista.urlImagem = url

        val dentistaJson = gson.toJson(dentista)
        val responseDentistaUpdate = http.put("/dentistas/${dentista.id}", dentistaJson)

        return gson.fromJson(responseDentistaUpdate, Dentista::class.java)
    }
}
