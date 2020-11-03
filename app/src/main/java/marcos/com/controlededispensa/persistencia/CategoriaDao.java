package marcos.com.controlededispensa.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import marcos.com.controlededispensa.modelo.Categoria;

@Dao
public interface CategoriaDao {

    @Insert
    long insert(Categoria cat);

    @Delete
    void delete(Categoria cat);

    @Update
    void update(Categoria cat);

    @Query("SELECT * FROM categorias WHERE id = :id")
    Categoria queryForId(long id);

    @Query("SELECT * FROM categorias ORDER BY descricao ASC")
    List<Categoria> queryAll();

    @Query("SELECT * FROM categorias WHERE descricao = :descricao ORDER BY descricao ASC")
    List<Categoria> queryForDescricao(String descricao);

    @Query("SELECT count(*) FROM categorias")
    int total();

}
