package net.daan.odontoapp.model

class Dentista {
    var id = ""
    var nome = ""
    var cro = ""
    var email = ""
    var telefone = ""
    var urlImagem = ""

    override fun toString(): String {
        return "Dentista(id='$id', nome='$nome', cro='$cro', email='$email', telefone='$telefone', urlImagem='$urlImagem')"
    }
}