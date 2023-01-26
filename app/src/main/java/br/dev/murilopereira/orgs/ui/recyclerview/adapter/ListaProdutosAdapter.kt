package br.dev.murilopereira.orgs.ui.recyclerview.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import br.dev.murilopereira.orgs.R
import br.dev.murilopereira.orgs.database.AppDatabase
import br.dev.murilopereira.orgs.databinding.ProdutoItemBinding
import br.dev.murilopereira.orgs.model.Produto
import br.dev.murilopereira.orgs.ui.activity.FormularioProdutoActivity
import coil.load
import java.text.NumberFormat
import java.util.*

class ListaProdutosAdapter(
    private val context: Context,
    produtos: List<Produto> = emptyList(),
    val onClick: (produto: Produto) -> Unit = {}
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {
    private val produtos = produtos.toMutableList()

    inner class ViewHolder(binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var produto: Produto;

        init {
            itemView.setOnClickListener {
                if (::produto.isInitialized) {
                    onClick(produto)
                }
            }
            itemView.setOnLongClickListener {
                PopupMenu(context, binding.root).apply {
                    setOnMenuItemClickListener { item: MenuItem ->
                        onMenuItemClick(item, produto)
                    }
                    inflate(R.menu.menu_detalhes_produto)
                    show()
                }

                return@setOnLongClickListener true;
            }
        }

        val nome = binding.produtoItemNome;
        val descricao = binding.produtoItemDescricao;
        val valor = binding.produtoItemValor;
        val imageView = binding.produtoItemImagem
        val currencyInstance: NumberFormat = NumberFormat
            .getCurrencyInstance(Locale("pt", "br"))
        fun vincula(produto: Produto) {
            this.produto = produto;

            nome.text = produto.nome;
            descricao.text = produto.descricao;
            valor.text = currencyInstance.format(produto.valor);
            imageView.load(produto.imagem)
        }
    }

    fun onMenuItemClick(item: MenuItem, produto: Produto): Boolean {
        val dao = AppDatabase.instance(context).produtoDao()
        return when (item.itemId) {
            R.id.menu_detalhes_produto_editar -> {
                Intent(context, FormularioProdutoActivity::class.java).apply {
                    putExtra("produtoId", produto.id)
                    startActivity(context, this, null)
                }
                true
            }
            R.id.menu_detalhes_produto_excluir -> {
                dao.remover(produto)
                true
            }
            else -> false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProdutoItemBinding
            .inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        return ViewHolder(binding);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position];
        holder.vincula(produto);
    }

    override fun getItemCount(): Int = produtos.size

    fun atualiza(produtos: List<Produto>) {
        this.produtos.clear();
        this.produtos.addAll(produtos)
        this.notifyDataSetChanged()
    }

}
