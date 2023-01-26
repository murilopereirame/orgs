package br.dev.murilopereira.orgs.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle;
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import br.dev.murilopereira.orgs.R
import br.dev.murilopereira.orgs.database.AppDatabase
import br.dev.murilopereira.orgs.databinding.ActivityListaProdutosBinding
import br.dev.murilopereira.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import coil.Coil
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.imageLoader
import coil.memory.MemoryCache

class ListaProdutosActivity : AppCompatActivity() {
    private val adapter = ListaProdutosAdapter(context = this) {
    produto ->
        val intent = Intent(this, DetalhesProdutoActivity::class.java)
        intent.putExtra("produtoId", produto.id)
        startActivity(intent)
    }

    private val binding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        configureRecyclerView()
        configuraFab()
        configureCoil()

        setContentView(binding.root)
    }

    private fun configureCoil() {
        val imageLoader = imageLoader
            .newBuilder()
            .placeholder(R.drawable.placeholder)
            .fallback(R.drawable.imagem_padrao)
            .error(R.drawable.erro)
            .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }.memoryCache {
            MemoryCache.Builder(this)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(this.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }
        .build()
        Coil.setImageLoader(imageLoader)
    }

    override fun onResume() {
        super.onResume()
        adapter.atualiza(AppDatabase.instance(this).produtoDao().buscaTodos())
    }

    private fun configuraFab() {
        val fab = binding.activityListaProdutoFab
        Log.i("ListaProdutos", "configuraFab: ${fab.id}")
        fab.setOnClickListener {
            navegarParaFormularioCriarProduto()
        }
    }

    private fun navegarParaFormularioCriarProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }

    private fun configureRecyclerView() {
        val recyclerView = binding.activityListaProdutoRecyclerView
        recyclerView.adapter = adapter
    }
}