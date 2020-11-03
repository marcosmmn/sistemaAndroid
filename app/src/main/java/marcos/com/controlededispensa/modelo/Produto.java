package marcos.com.controlededispensa.modelo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "produtos",foreignKeys = @ForeignKey(entity = Categoria.class,
                                                            parentColumns =  "id",
                                                            childColumns = "catId"))
public class Produto {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String nome;

    @NonNull
    private String marca;

    @NonNull
    private String validade;

    @NonNull
    private double qtd;

    @NonNull
    private String unidade;

    @ColumnInfo(index = true)
    private int catId;

    @NonNull
    private String armazenamento;

    @Ignore
    private String nomeCategoria;

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;

    }

    public Produto(String nome){setNome(nome);}

    public int getId() {
        return id;
    }

    public Produto(){

    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public void setNome(@NonNull  String nome) {
         this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public double getQtd() {
        return qtd;
    }

    public void setQtd(double qtd) {
        this.qtd = qtd;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;

    }

    public String getArmazenamento() {
        return armazenamento;
    }

    public void setArmazenamento(String armazenamento) {
        this.armazenamento = armazenamento;
    }

    @Override
    public String toString() {
        return "Product: " + nome + "\n" +
                "Mark: " + marca + "\n" +
                "Shelf Life: " + validade + "\n" +
                "Quantity: " + qtd +
                " "+ unidade + "\n" +
                "Category: " + nomeCategoria + "\n" + //no aplicativo a listagem vai aparecer a categoria com a numeração, não sei como trazer a descrição pra esse canto, ou se é possivel
                "Storage: " + armazenamento;
    }
}
