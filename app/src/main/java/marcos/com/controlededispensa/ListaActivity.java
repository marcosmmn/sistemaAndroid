package marcos.com.controlededispensa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaActivity extends AppCompatActivity {

    private ListView listViewProdutos;
    public static final int CODE = 1;
    private ArrayList<Produto> produtos;
    private ArrayAdapter<Produto> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

    listViewProdutos = findViewById(R.id.listViewProdutos);

    listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Produto prod =(Produto) listViewProdutos.getItemAtPosition(i);

            Toast.makeText(getApplicationContext(),getString(R.string.o_produto)+prod.getNome()+
                    getString(R.string.com_validade)+prod.getValidade()+getString(R.string.possui)+prod.getQtd()+
                    " "+prod.getUnidade()+ getString(R.string.armazenado),Toast.LENGTH_LONG).show();
                 }
            });

        popularLista();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal_opcoes,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemAdicionar:

               return true;
            case R.id.menuItemSobre:

                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void adicionar(MenuItem item){
        Intent intent = new Intent(this,MainActivity.class);

        startActivityForResult(intent,CODE);

    }

    public void acessarSobre(MenuItem item){
        Intent intent = new Intent(this,ActivityAutoria.class);

        startActivity(intent);
    }

    private void popularLista(){
            produtos = new ArrayList<>();
            adapter = new ArrayAdapter<>(this,android.R.layout.
                          simple_list_item_1,produtos);
            listViewProdutos.setAdapter(adapter);



    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {
        if(requestCode == CODE && resultCode == Activity.RESULT_OK) {
            Produto produto = new Produto();
            Bundle bundle = data.getExtras();
            if(bundle!=null){
                produto.setNome(bundle.getString(MainActivity.NOME));
                produto.setMarca(bundle.getString(MainActivity.MARCA));
                produto.setValidade(bundle.getString(MainActivity.VALIDADE));
                produto.setQtd(Integer.parseInt(bundle.getString(MainActivity.QTD)));
                produto.setUnidade(bundle.getString(MainActivity.UNID));
                produto.setCategoria(bundle.getString(MainActivity.CATEGORIA));
                produto.setArmazenamento(bundle.getString(MainActivity.ARMAZ));

                produtos.add(produto);

                adapter.notifyDataSetChanged();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}