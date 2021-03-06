package com.example.amministratore.boa_viagem.provider;

import android.net.Uri;

/**
 * Created by Amministratore on 18/08/2017.
 */


public class BoaViagemContract{



    public static final String AUTHORITY = "com.example.amministratore.boa_viagem.provider.viagem.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static final String VIAGEM_PATH = "tb_viagem";
    public static final String GASTO_PATH = "tb_gasto";



    public static final class Viagem{

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" +
                        "vnd.com.example.amministratore.boa_viagem.provider.viagem.provider/tb_viagem";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" +
                        "vnd.com.example.amministratore.boa_viagem.provider.viagem.provider/tb_viagem";
        public static final Uri CONTENT_URI =Uri.withAppendedPath(AUTHORITY_URI, VIAGEM_PATH);
        public static final String _ID = "_id";
        public static final String DESTINO = "destino";
        public static final String DATA_CHEGADA = "data_chegada";
        public static final String DATA_SAIDA = "data_saida";
        public static final String ORCAMENTO = "orcamento";
        public static final String QUANTIDADE_PESSOAS ="quantidade_pessoas";


    }
    public static  final class Gasto{

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/"+
                "vnd.com.example.amministratore.boa_viagem.provider/tb_gasto";
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" +
                        "vnd.com.example.amministratore.boa_viagem.provider/tb_gasto";
        public static final Uri CONTENT_URI =  Uri.withAppendedPath(AUTHORITY_URI, GASTO_PATH);
        public static final String _ID = "_id";
        public static final String VIAGEM_ID = "viagem_id";
        public static final String CATEGORIA = "categoria";
        public static final String DATA = "data";
        public static final String DESCRICAO = "descricao";
        public static final String LOCAL = "local";
    }






}
