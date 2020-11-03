package marcos.com.controlededispensa;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;

import java.util.ArrayList;
import java.util.List;

import marcos.com.controlededispensa.modelo.Categoria;
import marcos.com.controlededispensa.modelo.Produto;
import marcos.com.controlededispensa.persistencia.ProdutosDatabase;
import marcos.com.controlededispensa.utils.UtilsGUI;

public class ListaActivity extends AppCompatActivity {

    private static final int REQUEST_NOVO_PRODUTO    = 1;
    private static final int REQUEST_ALTERAR_PRODUTO = 2;

    private ListView listViewProdutos;
    private ArrayList<Produto> produtos;
    private ArrayAdapter<Produto> adapter;

    private ActionMode actionMode;
    private int posicaoSelecionada = -1;
    private View viewSelecionada;

    //Mudar para o modo noturno *SharedPreferences*
    private static final String ARQUIVO = "marcos.com.controlededispensa.PREFERENCIAS";
    private static final String MODO = "MODO";
    private int opcao = AppCompatDelegate.MODE_NIGHT_YES;

    private List<Produto> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        listViewProdutos = findViewById(R.id.listViewProdutos);

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Produto produto = (Produto) adapterView.getItemAtPosition(position);
                MainActivity.alterarProduto(ListaActivity.this,REQUEST_ALTERAR_PRODUTO,produto);



            }
        });

        listViewProdutos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        popularLista();
        lerPreferenciaModo();
        registerForContextMenu(listViewProdutos);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.principal_item_selecionado, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info;

        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

       Produto produto = (Produto) listViewProdutos.getItemAtPosition(info.position);

        switch(item.getItemId()){

            case R.id.menuItemEditar:
                MainActivity.alterarProduto(this,
                        REQUEST_ALTERAR_PRODUTO,
                        produto);
                return true;

            case R.id.menuItemExcluir:
                excluirProduto(produto);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item;
        switch (opcao) {
            case 2:
                item = menu.findItem(R.id.modoNot);
                break;
            case 1:
                item = menu.findItem(R.id.modoCla);
                break;
            default:
                return false;
        }
        item.setChecked(true);
        return true;
    }

    private void lerPreferenciaModo() {
        SharedPreferences shared = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        opcao = shared.getInt(MODO, opcao);
        mudaModo();
    }

    private void mudaModo() {
        if (opcao == 1) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
        } else if (opcao == 2) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);
        }
    }

    private void salvarPreferenciaOrdem(int novoValor) {
        SharedPreferences shared = getSharedPreferences(ARQUIVO,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putInt(MODO, novoValor);

        editor.commit();

        opcao = novoValor;

        mudaModo();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemAdicionar:
                verificaTipos();
                return true;
            case R.id.menuItemSobre:
                return true;
            case R.id.modoNot:
                salvarPreferenciaOrdem(2);
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_NO);
                return true;
            case R.id.modoCla:
                salvarPreferenciaOrdem(1);
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_YES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    private void verificaTipos(){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ProdutosDatabase database = ProdutosDatabase.getDatabase(ListaActivity.this);

                int total = database.categoriaDao().total();

                if (total == 0){

                    ListaActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UtilsGUI.avisoErro(ListaActivity.this, R.string.nenhum_cat);
                        }
                    });

                    return;
                }

                MainActivity.novoProduto(ListaActivity.this, REQUEST_NOVO_PRODUTO);
            }
        });
    }

    public void excluirProduto(final Produto produto) {
        String mensagem = getString(R.string.deseja_realmente_apagar)
                + "\n" + produto.getNome();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        ProdutosDatabase database =
                                                ProdutosDatabase.getDatabase(ListaActivity.this);

                                        database.produtoDao().delete(produto);

                                        ListaActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.remove(produto);
                                            }
                                        });
                                    }
                                });

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

        UtilsGUI.confirmaAcao(this, mensagem, listener);
    }

    ;

    //acessar informações do app
    public void acessarSobre(MenuItem item) {
        Intent intent = new Intent(this, ActivityAutoria.class);

        startActivity(intent);
    }

    //exibir dados cadastrados
    private void popularLista() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ProdutosDatabase database = ProdutosDatabase.getDatabase(ListaActivity.this);

                ArrayList<Categoria> categorias = (ArrayList<Categoria>) database.categoriaDao().queryAll();
                lista = database.produtoDao().queryAll();

                final ArrayList<Produto> produtos = new ArrayList<>();
                for(int i=0;i<lista.size();i++){
                    Produto prod = new Produto();
                    prod.setId(lista.get(i).getId());
                    prod.setNome(lista.get(i).getNome());
                    prod.setMarca(lista.get(i).getMarca());
                    prod.setValidade(lista.get(i).getValidade());
                    prod.setQtd(lista.get(i).getQtd());
                    prod.setUnidade(lista.get(i).getUnidade());
                    for(int j=0;j<categorias.size();j++){
                        if(lista.get(i).getCatId() == categorias.get(j).getId()){
                            prod.setNomeCategoria(categorias.get(j).getDescricao());
                        } // recurso utilizado para retornar o nome da categoria
                    }
                    prod.setArmazenamento(lista.get(i).getArmazenamento());
                    produtos.add(prod);
                }

                ListaActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ArrayAdapter<>(ListaActivity.this,
                                R.layout.row,
                                produtos);

                        listViewProdutos.setAdapter(adapter);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_NOVO_PRODUTO || requestCode == REQUEST_ALTERAR_PRODUTO)
                && resultCode == Activity.RESULT_OK) {

            popularLista();
        }
    }
}