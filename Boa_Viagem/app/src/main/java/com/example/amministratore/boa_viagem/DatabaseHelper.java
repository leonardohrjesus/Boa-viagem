package com.example.amministratore.boa_viagem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Amministratore on 16/07/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "BoaViagem";
    private static int VERSAO = 1;


    // questo metodo faz a conexao com banco de dados
    public DatabaseHelper(Context context) {
        super(context, BANCO_DADOS, null, VERSAO);
    }


    //meotodo de criacao das tabelas de  viagens e gasto
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE viagem (_id INTEGER PRIMARY KEY," +
                " destino TEXT, tipo_viagem INTEGER, data_chegada DATE," +
                " data_saida DATE, orcamento DOUBLE," +
                " quantidade_pessoas INTEGER);");
        db.execSQL("CREATE TABLE gasto (_id INTEGER PRIMARY KEY," +
                " categoria TEXT, data DATE, valor DOUBLE," +
                " descricao TEXT, local TEXT, viagem_id INTEGER," +
                " FOREIGN KEY(viagem_id) REFERENCES viagem(_id));");


    }

    //questo metodo serve quando existir uma versao das tabelas ou um campo
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {}
}
