package com.example.amministratore.boa_viagem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

/**
 * Created by Amministratore on 11/07/2017.
 */

public class GastoActivity extends AppCompatActivity {

    private  Spinner categoria;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gasto);

       /* this, R.array.categoria_gasto,  android.R.layout.simple_spinner_item);
        categoria = (Spinner) findViewById(R.id.categoria);
        categoria.setAdapter(adapter);*/
    }
}
