package br.dev.murilopereira.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.dev.murilopereira.orgs.database.converter.Converters
import br.dev.murilopereira.orgs.database.dao.ProdutoDao
import br.dev.murilopereira.orgs.model.Produto

@Database(entities = [Produto::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao

    companion object {
        @Volatile
        private lateinit var db: AppDatabase

        fun instance(context: Context): AppDatabase {
            if(::db.isInitialized) return db

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "orgs.db"
            ).allowMainThreadQueries().build().also {
                db = it
            }
        }
    }
}