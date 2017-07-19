package com.example.amministratore.boa_viagem;




import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.Calendar;


import static com.example.amministratore.boa_viagem.R.id.dataChegada;
import static com.example.amministratore.boa_viagem.R.id.dataSaida;


/**
 * Created by Amministratore on 11/07/2017.
 */

public class ViagemActivity extends AppCompatActivity {
    private int ano, mes, dia;
    private int ano1, mes1, dia1;
    private String id;
    private Button dataChegadaButton;
    private Button dataSaidaButton;

    private DatabaseHelper helper;
    private EditText destino, quantidadePessoas, orcamento;
    private RadioGroup radioGroup;



    //metodo de criacao
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_viagem);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        Calendar calendar1 = Calendar.getInstance();
        ano1 = calendar1.get(Calendar.YEAR);
        mes1 = calendar1.get(Calendar.MONTH);
        dia1 = calendar1.get(Calendar.DAY_OF_MONTH);




        dataChegadaButton = (Button) findViewById(dataChegada);
        dataChegadaButton.setText(dia + "/" + (mes+1) + "/" + ano);
        dataSaidaButton = (Button) findViewById(dataSaida);
        dataSaidaButton.setText(dia1 + "/" + (mes1+1) + "/" + ano1);
        // recuperando novas views
        destino = (EditText) findViewById(R.id.destino);
        quantidadePessoas = (EditText) findViewById(R.id.quantidadePessoas);
        orcamento = (EditText) findViewById(R.id.orcamento);
        radioGroup = (RadioGroup) findViewById(R.id.tipoViagem);

        // prepara acesso ao banco de dados
        helper = new DatabaseHelper(this);


        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);

        if(id != null){
            prepararEdicao();
        }


    }

    public void selecionarData(View view){
        showDialog(view.getId());
    }

   @Override
    protected Dialog onCreateDialog(int id) {
        if(dataChegada == id ){
            return new DatePickerDialog(this, listener, ano, mes, dia);
        }

        else if( dataSaida == id){
            return new DatePickerDialog(this, listener1, ano1, mes1, dia1);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,    int year, int monthOfYear, int dayOfMonth) {

                    ano = year;
                    mes = monthOfYear;
                    dia = dayOfMonth;
                    dataChegadaButton.setText(dia + "/" + (mes+1) + "/" + ano);

        }

    };
    private DatePickerDialog.OnDateSetListener listener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,    int year, int monthOfYear, int dayOfMonth) {


            ano1 = year;
            mes1 = monthOfYear;
            dia1 = dayOfMonth;
            dataSaidaButton.setText(dia1 + "/" + (mes1+1) + "/" + ano1);

        }

    };

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viagem_menu, menu);
        return true;
    }

    //selecionar opcao do menu
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.novo_gasto:
                startActivity(new Intent(this, GastoActivity.class));
                return true;
            case R.id.remover:
                //remover viagem do banco de dados
                return true;
        default:
            return super.onOptionsItemSelected(item);
     }
    }

    // METODO SALVA DADOS NA BASE
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void salvarViagem(View view) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("destino", destino.getText().toString());
        values.put("data_chegada", (String) dataChegadaButton.getText());
        values.put("data_saida", (String) dataSaidaButton.getText());
        values.put("orcamento", orcamento.getText().toString());
        values.put("quantidade_pessoas",   quantidadePessoas.getText().toString());

        int tipo = radioGroup.getCheckedRadioButtonId();
        if(tipo == R.id.lazer) {
            values.put("tipo_viagem", Constantes.VIAGEM_LAZER);
        } else {
            values.put("tipo_viagem", Constantes.VIAGEM_NEGOCIOS);
        }

        long resultado = db.insert("viagem", null, values);
        if(resultado != -1 ){
            Toast.makeText(this, getString(R.string.registro_salvo),
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, getString(R.string.erro_salvar),
                    Toast.LENGTH_SHORT).show();
        }

    }


    private void prepararEdicao() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor =
                db.rawQuery("SELECT tipo_viagem, destino, data_chegada, " +
                        "data_saida, quantidade_pessoas, orcamento " +
                        "FROM viagem WHERE _id = ?", new String[]{ id });
        cursor.moveToFirst();
        if(cursor.getInt(0) == Constantes.VIAGEM_LAZER){
            radioGroup.check(R.id.lazer);
        } else {
            radioGroup.check(R.id.negocios);
        }
        destino.setText(cursor.getString(1));
        String dataChegada = cursor.getString(2);
        String dataSaida = cursor.getString(3);
        dataChegadaButton.setText(dataChegada);
        dataSaidaButton.setText(dataSaida);
        quantidadePessoas.setText(cursor.getString(4));
        orcamento.setText(cursor.getString(5));
        cursor.close();
    }

    //fechar conexao com banco
    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }

}
