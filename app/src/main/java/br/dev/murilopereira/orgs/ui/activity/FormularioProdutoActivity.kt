package br.dev.murilopereira.orgs.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.dev.murilopereira.orgs.database.AppDatabase
import br.dev.murilopereira.orgs.databinding.ActivityFormularioProdutoBinding
import br.dev.murilopereira.orgs.model.Produto
import br.dev.murilopereira.orgs.ui.dialog.FormularioImagemDialog
import coil.load
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }
    private val dao by lazy {
       AppDatabase.instance(this).produtoDao()
    }
    private var url: String? = null
    private var idProduto = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        configuraBotaoSalvar()
        carregarInfo()

        title = if(idProduto > 0) "Alterar Produto" else "Cadastrar Produto"

        setContentView(binding.root)
        binding.activityFormularioProdutoImagem.setOnClickListener {
            FormularioImagemDialog(this).show(url) { url ->
                this.url = url
                binding.activityFormularioProdutoImagem.load(this.url)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dao.buscaId(idProduto)?.let {preencheCampos(it)}
    }

    private fun carregarInfo() {
        intent.getLongExtra("produtoId", 0)?.let { produtoId ->
            idProduto = produtoId
        }
    }

    private fun preencheCampos(produto: Produto) {
        url = produto.imagem
        binding.activityFormularioProdutoNome.setText(produto.nome)
        binding.activityFormularioProdutoValor.setText(produto.valor.toPlainString())
        binding.activityFormularioProdutoDescricao.setText(produto.descricao)
        binding.activityFormularioProdutoImagem.load(produto.imagem)
    }

    private fun configuraBotaoSalvar() {
        val btnSalvar = binding.btnSalvar
        btnSalvar.setOnClickListener {
            acaoBotaoSalvar()
        }
    }

    private fun acaoBotaoSalvar() {
        val produtoNovo = criaProduto()

        dao.salvar(produtoNovo)

        finish()
    }

    private fun criaProduto(): Produto {
        val campoNome = binding.activityFormularioProdutoNome
        val nome = campoNome.text.toString();

        val campoDescricao = binding.activityFormularioProdutoDescricao
        val descricao = campoDescricao.text.toString();

        val campoValor = binding.activityFormularioProdutoValor
        val valor = if (campoValor.text.toString().isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(campoValor.text.toString())
        }

        return Produto(
            id = idProduto,
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url
        );
    }
}