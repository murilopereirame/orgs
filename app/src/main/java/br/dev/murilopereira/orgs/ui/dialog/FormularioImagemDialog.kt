package br.dev.murilopereira.orgs.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import br.dev.murilopereira.orgs.databinding.FormularioImagemBinding
import coil.load

class FormularioImagemDialog(private val context: Context) {
    fun show(defaultURL: String? = null, whenLoaded: (url: String) -> Unit) {
        FormularioImagemBinding.inflate(LayoutInflater.from(context)).apply {
            defaultURL?.let {
                formularioImagemImageview.load(defaultURL)
                formularioImagemUrl.setText(defaultURL)
            }

            formularioImagemBotaoCarregar.setOnClickListener {
                val url = formularioImagemUrl.text.toString()
                formularioImagemImageview.load(url)
            }

            AlertDialog.Builder(context)
                .setView(root)
                .setPositiveButton("Confirmar") { _, _ ->
                    val url = formularioImagemUrl.text.toString()
                    whenLoaded(url)
                }
                .setNegativeButton("Cancelar") { _, _ -> }
                .show()
        }
    }
}