package marcos.com.controlededispensa;

public class Produto {

    private String nome;

    private String marca;

    private String validade;

    private int qtd;

    private String unidade;

    private String categoria;

    private String armazenamento;

    public Produto(){}

    public Produto(String nome, String marca, String validade, int qtd, String unidade, String categoria, String armazenamento) {

        this.nome = nome;
        this.marca = marca;
        this.validade = validade;
        this.qtd = qtd;
        this.unidade = unidade;
        this.categoria = categoria;
        this.armazenamento = armazenamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
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

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getArmazenamento() {
        return armazenamento;
    }

    public void setArmazenamento(String armazenamento) {
        this.armazenamento = armazenamento;
    }

    @Override
    public String toString() {
        return
                "Produto: " + nome + "\n" +
                "Marca: " + marca + "\n" +
                "Validade: " + validade + "\n" +
                "Quantidade: " + qtd +
                " "+ unidade + "\n" +
                "Categoria: " + categoria + "\n" +
                "Armazenamento: " + armazenamento;
    }
}
