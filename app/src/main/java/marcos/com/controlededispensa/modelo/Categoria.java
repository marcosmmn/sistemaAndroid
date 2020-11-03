package marcos.com.controlededispensa.modelo;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "categorias",
        indices = @Index(value = {"descricao"},unique = true))
public class Categoria {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String descricao;

    @Ignore
    private Drawable icone;

    public Drawable getIcone() {
        return icone;
    }

    public void setIcone(Drawable icone) {
        this.icone = icone;
    }

    public Categoria(String descricao){
        setDescricao(descricao);
    }

    public Categoria(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(@NonNull String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return getDescricao();
    }
}
