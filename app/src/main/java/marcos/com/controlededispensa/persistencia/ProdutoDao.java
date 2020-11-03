package marcos.com.controlededispensa.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import marcos.com.controlededispensa.modelo.Produto;

@Dao
public interface ProdutoDao {

    @Insert
    long insert(Produto produto);

    @Delete
    void delete(Produto produto);

    @Update
    void update(Produto produto);

    @Query("SELECT * FROM produtos WHERE id = :id")
    Produto queryForId(long id);

    @Query("SELECT * FROM produtos ORDER BY nome ASC")
    List<Produto> queryAll();

    @Query("SELECT * FROM produtos WHERE catId = :id ORDER BY nome ASC")
    List<Produto> queryForCatId(long id);
}
