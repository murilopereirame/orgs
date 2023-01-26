package br.dev.murilopereira.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.dev.murilopereira.orgs.R
import br.dev.murilopereira.orgs.database.AppDatabase
import br.dev.murilopereira.orgs.databinding.ActivityDetalhesProdutoBinding
import br.dev.murilopereira.orgs.model.Produto
import coil.load
import java.text.NumberFormat
import java.util.*

class DetalhesProdutoActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }
    private val dao by lazy {
        AppDatabase.instance(this).produtoDao()
    }

    private var produtoId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carregarInformacoes()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        dao.buscaId(this.produtoId)?.let {
            preencheCampos(it)
        } ?: finish()
    }

    private fun preencheCampos(produto: Produto) {
        val imageView = binding.activityDetalhesProdutoImagem
        val nome = binding.activityDetalhesProdutoNome
        val descricao = binding.activityDetalhesProdutoDescricao
        val preco = binding.activityDetalhesProdutoPreco
        val currencyInstance: NumberFormat = NumberFormat
            .getCurrencyInstance(Locale("pt", "br"))

        nome.text = produto.nome;
        descricao.text = produto.descricao;
        preco.text = currencyInstance.format(produto.valor);
        imageView.load(produto.imagem)
    }

    private fun carregarInformacoes() {
        this.produtoId = intent.getLongExtra("produtoId", 0)
        if(this.produtoId == 0L)
            finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_detalhes_produto_editar -> {
                Intent(this, FormularioProdutoActivity::class.java).apply {
                    putExtra("produtoId", produtoId)
                    startActivity(this)
                }
            }
            R.id.menu_detalhes_produto_excluir -> {
                dao.removerId(produtoId)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }
}