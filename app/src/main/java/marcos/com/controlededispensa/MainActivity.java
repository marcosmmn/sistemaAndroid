package marcos.com.controlededispensa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCategoria;
    private Button buttonLimpar;
    private EditText editTextNome, editTextMarca, editTextValidade, editTextQtd;
    private RadioGroup radioGroupUnid;
    private CheckBox checkBoxArmario, checkBoxGeladeira;
    public static final String NOME = "NOME";
    public static final String MARCA = "MARCA";
    public static final String VALIDADE = "VALIDADE";
    public static final String QTD = "QTD";
    public static final String UNID = "UNID";
    public static final String CATEGORIA = "CATEGORIA";
    public static final String ARMAZ = "ARMAZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        buttonLimpar = findViewById(R.id.buttonLimpar);
        editTextNome = findViewById(R.id.editTextNome);
        editTextQtd = findViewById(R.id.editTextQtd);
        editTextMarca = findViewById(R.id.editTextMarca);
        editTextValidade = findViewById(R.id.editTextValidade);
        radioGroupUnid = findViewById(R.id.radioGroupUnid);
        checkBoxGeladeira = findViewById(R.id.checkBoxGeladeira);
        checkBoxArmario = findViewById(R.id.checkBoxArmario);

        popularSpinner();
    }

    public void popularSpinner() {
        ArrayList<String> lista = new ArrayList<>();
        lista.add(getString(R.string.selecione_spinner));
        lista.add(getString(R.string.salada));
        lista.add(getString(R.string.carne));
        lista.add(getString(R.string.tempero));
        lista.add(getString(R.string.peixe));
        lista.add(getString(R.string.farinha));
        lista.add(getString(R.string.massa));
        lista.add(getString(R.string.bebidas));
        lista.add(getString(R.string.enlatados));
        lista.add(getString(R.string.frango));
        lista.add(getString(R.string.porco));
        lista.add(getString(R.string.graos));
        lista.add(getString(R.string.guloseima));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, lista);

        spinnerCategoria.setAdapter(adapter);

    }

    public void limpar(View view) {
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

    public void salvar(View view) {
        String nome = editTextNome.getText().toString();
        String marca = editTextMarca.getText().toString();
        String validade = editTextValidade.getText().toString();
        String qtd = editTextQtd.getText().toString();
        String unid = verificaRadio(radioGroupUnid);
        String categoria = verificaSpinner(spinnerCategoria);
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

        } else if (categoria == null || categoria.trim().isEmpty()) {
            Toast.makeText(this, R.string.sem_cat, Toast.LENGTH_LONG).show();

        } else if (armaz == null || armaz.trim().isEmpty()) {
            Toast.makeText(this, R.string.sem_arm, Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(this, getString(R.string.prod_cad) + getString(R.string.toast_nome) + nome + getString(R.string.toast_marca) + marca
                    + getString(R.string.toast_val) + validade + getString(R.string.toast_qtd) + qtd + " " + unid
                    + getString(R.string.toast_cat) + categoria + getString(R.string.toast_arm) + armaz, Toast.LENGTH_LONG).show();

            Intent intent = new Intent();

            intent.putExtra(NOME,nome);
            intent.putExtra(MARCA,marca);
            intent.putExtra(VALIDADE,validade);
            intent.putExtra(QTD,qtd);
            intent.putExtra(UNID,unid);
            intent.putExtra(CATEGORIA,categoria);
            intent.putExtra(ARMAZ,armaz);

            setResult(Activity.RESULT_OK,intent);

            finish();
        }



    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
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

    public String verificaSpinner(Spinner spinner) {
        String cat = (String) spinner.getSelectedItem();
        String mensagem = "";
        if (cat.equals(getString(R.string.selecione_spinner))) {
            mensagem = "";
        } else if (cat != null) {

            mensagem = cat;
        }
        return mensagem;
    }

}