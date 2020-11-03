package marcos.com.controlededispensa.persistencia;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

import marcos.com.controlededispensa.R;
import marcos.com.controlededispensa.modelo.Categoria;
import marcos.com.controlededispensa.modelo.Produto;

@Database(entities = {Produto.class, Categoria.class},version = 1)
public abstract class ProdutosDatabase extends RoomDatabase {

    public abstract ProdutoDao produtoDao();

    public abstract CategoriaDao categoriaDao();

    private static ProdutosDatabase instance;

    public static ProdutosDatabase getDatabase(final Context context){
        if(instance == null){
            synchronized (ProdutosDatabase.class){
                RoomDatabase.Builder builder = Room.databaseBuilder(context,
                                                                    ProdutosDatabase.class,
                                                                    "produtos.db");

                builder.addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                carregaCategoriasIniciais(context);
                            }
                        });
                    }
                });
                instance = (ProdutosDatabase) builder.build();

            }
        }

    return instance;
    }

    private static void carregaCategoriasIniciais(final Context context){
        String[] descricoes = context.getResources().getStringArray(R.array.cat);


        for(int i = 0;i<descricoes.length;i++){
             Categoria categoria = new Categoria();
             categoria.setDescricao(descricoes[i]);
             instance.categoriaDao().insert(categoria);

        }
    }

}
