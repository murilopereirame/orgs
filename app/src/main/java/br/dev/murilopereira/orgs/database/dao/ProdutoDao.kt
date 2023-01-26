package br.dev.murilopereira.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.dev.murilopereira.orgs.model.Produto

@Dao
interface ProdutoDao {
    @Query("SELECT * FROM Produto")
    fun buscaTodos(): List<Produto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salvar(vararg produto: Produto)

    @Delete
    fun remover(produto: Produto)

    @Query("DELETE FROM Produto WHERE id=:id")
    fun removerId(id: Long)

    @Query("SELECT * FROM Produto WHERE id= :id")
    fun buscaId(id: Long): Produto?
}