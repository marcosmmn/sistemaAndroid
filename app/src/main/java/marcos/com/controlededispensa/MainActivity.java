package marcos.com.controlededispensa;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import marcos.com.controlededispensa.modelo.Categoria;
import marcos.com.controlededispensa.modelo.ItensAdapter;
import marcos.com.controlededispensa.modelo.Produto;
import marcos.com.controlededispensa.persistencia.ProdutosDatabase;

public class MainActivity extends AppCompatActivity {
    //objetos do front
    private Spinner spinnerCategoria;
    private EditText editTextNome, editTextMarca, editTextValidade, editTextQtd;
    private RadioGroup radioGroupUnid;
    private CheckBox checkBoxArmario, checkBoxGeladeira;

    //constante de referencia do objeto produto
    public static final String ID    = "ID";

    //constantes para definir os modos que estão sendo operados
    public static final String MODO    = "MODO";
    public static final int    NOVO    = 1;
    public  static final int   ALTERAR = 2;
    private int modo;
    private Produto produto;

    private List<Categoria> listaCategoria;

    public static  void novoProduto(Activity activity, int requestCode){
        Intent intent = new Intent(activity,MainActivity.class);

        intent.putExtra(MODO,NOVO);

        activity.startActivityForResult(intent, requestCode);
    }

    public static void alterarProduto(Activity activity, int requestCode, Produto produto){
        Intent intent = new Intent(activity, MainActivity.class);

        intent.putExtra(MODO,ALTERAR);
        intent.putExtra(ID,produto.getId());


        activity.startActivityForResult(intent,requestCode);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        spinnerCategoria = findViewById(R.id.spinnerCategoria);

        editTextNome = findViewById(R.id.editTextNome);
        editTextQtd = findViewById(R.id.editTextQtd);
        editTextMarca = findViewById(R.id.editTextMarca);
        editTextValidade = findViewById(R.id.editTextValidade);
        radioGroupUnid = findViewById(R.id.radioGroupUnid);
        checkBoxGeladeira = findViewById(R.id.checkBoxGeladeira);
        checkBoxArmario = findViewById(R.id.checkBoxArmario);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        modo = bundle.getInt(MODO, NOVO);

        popularSpinner();

        if(modo == ALTERAR){
            setTitle(getString(R.string.altera_produto));

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    int id = bundle.getInt(ID);

                    ProdutosDatabase database = ProdutosDatabase.getDatabase(MainActivity.this);

                    produto = database.produtoDao().queryForId(id);

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editTextNome.setText(produto.getNome());
                            editTextMarca.setText(produto.getMarca());
                            editTextValidade.setText(produto.getValidade());
                            editTextQtd.setText(String.valueOf(produto.getQtd()));
                            retornaUnidade();
                            retornaArmaz();

                            int posicao = posicaoTipo(produto.getCatId());
                            spinnerCategoria.setSelection(posicao);
                        }
                    });
                }
            });


            }else{

                setTitle(R.string.novo_produto);

                produto = new Produto("");


            }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.produto_opcoes,menu);
        return true;
    }

    public void retornaArmaz(){
        if(produto.getArmazenamento().equals("Geladeira")){
            checkBoxGeladeira.setChecked(true);
        }else if(produto.getArmazenamento().equals("Armário")){
            checkBoxArmario.setChecked(true);
        }else if(produto.getArmazenamento().equals("Armário e Geladeira")){
            checkBoxArmario.setChecked(true);
            checkBoxGeladeira.setChecked(true);
        }
    }

    private int posicaoTipo(int tipoId){

        for (int pos = 0; pos < listaCategoria.size(); pos++){

            Categoria c = listaCategoria.get(pos);

            if (c.getId() == tipoId){
                return pos;
            }
        }

        return -1;
    }

    public void retornaUnidade(){
        if(produto.getUnidade().equals("Unidade(s)")){
            radioGroupUnid.check(R.id.radioButtonUnit);
        }else if (produto.getUnidade().equals("Quilo(s)")){
            radioGroupUnid.check(R.id.radioButtonKilos);
        }else if (produto.getUnidade().equals("Litro(s)")){
            radioGroupUnid.check(R.id.radioButtonLitros);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemSalvar:
                salvar();
                return true;
            case R.id.menuItemLimpar:
                return  true;
            case android.R.id.home:
                cancelar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    public void popularSpinner() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ProdutosDatabase database = ProdutosDatabase.getDatabase(MainActivity.this);

                listaCategoria = database.categoriaDao().queryAll();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        TypedArray icones = getResources().obtainTypedArray(R.array.imagens);

                        ArrayList<Categoria> cats = new ArrayList();

                        for (int cont = 0; cont < listaCategoria.size(); cont++){
                            Categoria catt = new Categoria();
                            catt.setId(listaCategoria.get(cont).getId());
                            catt.setDescricao(listaCategoria.get(cont).getDescricao());
                            catt.setIcone(icones.getDrawable(cont));
                            cats.add(catt);
                        }

                        ItensAdapter itensAdapter = new ItensAdapter(MainActivity.this, cats);

                        spinnerCategoria.setAdapter(itensAdapter);
                    }
                });
            }
        });

    }

    public void limpar(MenuItem item) {
        editTextNome.setText(null);
        editTextMarca.setText(null);
        editTextValidade.setText(null);
        editTextQtd.setText(null);
        checkBoxArmario.setChecked(false);
        checkBoxGeladeira.setChecked(false);
        radioGroupUnid.clearCheck();
        spinnerCategoria.setSelection(0);
        editTextNome.requestFocus();
        Toast.makeText(this, R.string.campos_limpos, Toast.LENGTH_LONG).show();
    }

    public void salvar() {
        String nome = editTextNome.getText().toString();
        String marca = editTextMarca.getText().toString();
        String validade = editTextValidade.getText().toString();
        String qtd = editTextQtd.getText().toString();
        String unid = verificaRadio(radioGroupUnid);
       // String categoria = verificaSpinner(spinnerCategoria);
        String armaz = verificaCheck(checkBoxArmario, checkBoxGeladeira);

        if (nome == null || nome.trim().isEmpty()) {
            Toast.makeText(this, R.string.camp_nome_vazio, Toast.LENGTH_LONG).show();
            editTextNome.requestFocus();
        } else if (marca == null || marca.trim().isEmpty()) {
            Toast.makeText(this, R.string.camp_marca_vazio, Toast.LENGTH_LONG).show();
            editTextMarca.requestFocus();
        } else if (validade == null || validade.trim().isEmpty()) {
            Toast.makeText(this, R.string.camp_val_vazio, Toast.LENGTH_LONG).show();
            editTextValidade.requestFocus();
        } else if (qtd == null || qtd.trim().isEmpty()) {
            Toast.makeText(this, R.string.sem_qtd, Toast.LENGTH_LONG).show();
            editTextQtd.requestFocus();
        } else if (unid == null || unid.trim().isEmpty()) {
            Toast.makeText(this, R.string.sem_unid, Toast.LENGTH_LONG).show();

        } else if (armaz == null || armaz.trim().isEmpty()) {
            Toast.makeText(this, R.string.sem_arm, Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(this, getString(R.string.prod_cad) + getString(R.string.toast_nome) + nome + getString(R.string.toast_marca) + marca
                    + getString(R.string.toast_val) + validade + getString(R.string.toast_qtd) + qtd + " " + unid
                     + getString(R.string.toast_arm) + armaz, Toast.LENGTH_LONG).show();

            produto.setNome(nome);
            produto.setMarca(marca);
            produto.setValidade(validade);
            produto.setUnidade(unid);
            produto.setQtd(Double.parseDouble(qtd));


            Categoria cat = (Categoria) spinnerCategoria.getSelectedItem();
            if (cat != null){
                produto.setCatId(cat.getId());
            }

            produto.setArmazenamento(armaz);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    ProdutosDatabase database = ProdutosDatabase.getDatabase(MainActivity.this);

                    if (modo == NOVO) {

                        database.produtoDao().insert(produto);

                    } else {

                        database.produtoDao().update(produto);
                    }

                    setResult(Activity.RESULT_OK);
                    finish();
                }
            });
        }



    }

    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        cancelar();
    }

    public String verificaCheck(CheckBox um, CheckBox dois) {
        String msg = "";
        if (um.isChecked() && dois.isChecked()) {
            msg = (String) um.getText() + " e " + (String) dois.getText();
        } else if (dois.isChecked()) {
            msg = (String) dois.getText();
        } else if (um.isChecked()) {
            msg = (String) um.getText();
        } else {
            msg = "";
        }
        return msg;
    }

    public String verificaRadio(RadioGroup radio) {
        switch (radio.getCheckedRadioButtonId()) {
            case R.id.radioButtonKilos:
                return getString(R.string.kilos);
            case R.id.radioButtonLitros:
                return getString(R.string.litros);
            case R.id.radioButtonUnit:
                return getString(R.string.unitario);
            default:
                return "";
        }
    }

}