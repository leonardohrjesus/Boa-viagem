package com.example.amministratore.boa_viagem;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Amministratore on 11/07/2017.
 */

public class DashboardActivity  extends AppCompatActivity {

    private String opcao;
    private TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.dashboard);

    }

    public void selecionarOpcao(View view) {
        switch (view.getId()) {
             case R.id.nova_viagem:
                 startActivity(new Intent(this, ViagemActivity.class));
                 textView = (TextView) view;
                 opcao = "Opção: " + textView.getText().toString();
                 Toast.makeText(this, opcao, Toast.LENGTH_LONG).show();
                 break;
            case R.id.novo_gasto:
                startActivity(new Intent(this, ViagemActivity.class));
                textView = (TextView) view;
                opcao = "Opção: " + textView.getText().toString();
                Toast.makeText(this, opcao, Toast.LENGTH_LONG).show();
                break;

            }


    }

}
