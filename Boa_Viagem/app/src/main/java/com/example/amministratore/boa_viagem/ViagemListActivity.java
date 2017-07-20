package com.example.amministratore.boa_viagem;


/**
 * Created by Amministratore on 14/07/2017.
 */

import android.app.ListActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.id;
import static com.example.amministratore.boa_viagem.R.id.dataChegada;
import static com.example.amministratore.boa_viagem.R.id.dataSaida;
import static com.example.amministratore.boa_viagem.R.id.quantidadePessoas;
import static com.example.amministratore.boa_viagem.R.string.destino;
import static com.example.amministratore.boa_viagem.R.string.orcamento;

public class ViagemListActivity extends ListActivity implements AdapterView.OnItemClickListener , DialogInterface.OnClickListener {


    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat;
    private Double valorLimite;
    private Intent intent;
    private AlertDialog dialogConfirmacao;
    private int viagemSelecionada;
    private AlertDialog alertDialog;
    private BoaViagemDAO dao;

    // atributo de instancia
    private List<Map<String, Object>> viagens;
    //metodo lista viagens
    private List<Map<String, Object>> listarViagens() {

        viagens = new ArrayList<Map<String, Object>>();

        List<Viagem> listaViagens = dao.listarViagens();

        for (Viagem viagem : listaViagens) {

            Map<String, Object> item = new HashMap<String, Object>();

            item.put("id", viagem.getId());

            if (viagem.getTipoViagem() == Constantes.VIAGEM_LAZER) {
                item.put("imagem", R.drawable.lazer);
            } else {
                item.put("imagem", R.drawable.negocios);
            }

            item.put("destino", viagem.getDestino());

            String periodo = dateFormat.format(viagem.getDataChegada()) + " a "
                    + dateFormat.format(viagem.getDataSaida());

            item.put("data", periodo);

            double totalGasto = dao.calcularTotalGasto(viagem);

            item.put("total", "Gasto total R$ " + totalGasto);

            double alerta = viagem.getOrcamento() * valorLimite / 100;
            Double [] valores = new Double[] { viagem.getOrcamento(), alerta, totalGasto };
            item.put("barraProgresso", valores);
            viagens.add(item);

        }
        return viagens;
    }

    //metodo de criacao
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new BoaViagemDAO(this);

        helper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SharedPreferences preferencias =  PreferenceManager.getDefaultSharedPreferences(this);
        String valor = preferencias.getString("valor_limite", "-1");
        valorLimite = Double.valueOf(valor);

        String[] de = { "imagem", "destino", "data",  "total", "barraProgresso" };
        int[] para = { R.id.tipoViagem, R.id.destino, R.id.data, R.id.valor, R.id.barraProgresso };
        SimpleAdapter adapter =   new SimpleAdapter(this, listarViagens(),   R.layout.lista_viagem, de, para);
        adapter.setViewBinder(new ViagemViewBinder());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        this.alertDialog = criaAlertDialog();
        this.dialogConfirmacao = criaDialogConfirmacao();

    }


    //metodo selciona a viagem
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.viagemSelecionada = position;
        alertDialog.show();
    }



    //metodo cria alert dialog com as opcoes
    private AlertDialog criaAlertDialog() {
        final CharSequence[] items = {
                getString(R.string.editar),
                getString(R.string.novo_gasto),
                getString(R.string.gastos_realizados),
                getString(R.string.remover) };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        builder.setItems(items, this);
        return builder.create();
    }

    //metodo criar e mostrar confirmacao de execlusao
    private AlertDialog criaDialogConfirmacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_viagem);
        builder.setPositiveButton(getString(R.string.sim),  this);
        builder.setNegativeButton(getString(R.string.nao),  this);
        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int item) {
        Intent intent;
        String id = (String) viagens.get(viagemSelecionada).get("id");
        switch (item) {
            case 0: // editar viagem
                intent = new Intent(this, ViagemActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 1:startActivity(new Intent(this, GastoActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, GastoListActivity.class));
                break;
            case 3:
                dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                viagens.remove(viagemSelecionada);
                removerViagem(id);
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;
            }
    }

    //classe responsalvel de controlar barra de progresso
    private class ViagemViewBinder implements SimpleAdapter.ViewBinder {


        @Override
        public boolean setViewValue(View view, Object data,  String textRepresentation) {

                if (view.getId() == R.id.barraProgresso) {
                    Double valores[] = (Double[]) data;
                    ProgressBar progressBar = (ProgressBar) view;
                    progressBar.setMax(valores[0].intValue());
                    progressBar.setSecondaryProgress(valores[1].intValue());
                    progressBar.setProgress(valores[2].intValue());
                    return true;
                }
                return false;
            }

    }

    private double calcularTotalGasto(SQLiteDatabase db, String id) {
        Cursor cursor = db.rawQuery(
                "SELECT SUM(valor) FROM gasto WHERE viagem_id = ?",
                new String[]{ id });
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        cursor.close();
        return total;
    }

    private void removerViagem(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where [] = new String[]{ id };
        db.delete("gasto", "viagem_id = ?", where);
        db.delete("viagem", "_id = ?", where);
        }



}
