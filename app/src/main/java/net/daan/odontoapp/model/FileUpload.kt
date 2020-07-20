package net.daan.odontoapp.model

class FileUpload {
    var fileName = ""
    var mimeType = ""
    var base64 = ""

    override fun toString(): String {
        return "FileUpload(fileName='$fileName', mimeType='$mimeType', base64='$base64')"
    }
}