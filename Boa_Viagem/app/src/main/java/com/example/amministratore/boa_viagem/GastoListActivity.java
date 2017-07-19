
package com.example.amministratore.boa_viagem;


import android.app.ListActivity;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GastoListActivity extends ListActivity implements AdapterView.OnItemClickListener , DialogInterface.OnClickListener {    private String dataAnterior = "";
    private List<Map<String, Object>> gastos;

    private AlertDialog dialogConfirmacao;
    private int gastoselecionado;


    //lista de gastos
    private List<Map<String, Object>> listarGastos() {
        gastos = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        item.put("data", "04/02/2012");
        item.put("descricao", "Diária Hotel");
        item.put("valor", "R$ 260,00");
        item.put("categoria", R.color.categoria_hospedagem);
        gastos.add(item);
// pode adicionar mais informações de viagens
        return gastos;
    }


    //metodo de criacao
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] de = { "data", "descricao", "valor", "categoria" };
        int[] para = { R.id.data, R.id.descricao, R.id.valor, R.id.categoria };
        SimpleAdapter adapter = new SimpleAdapter(this, listarGastos(),  R.layout.lista_gasto, de, para);
        adapter.setViewBinder(new GastoViewBinder());
        setListAdapter(adapter);
        getListView().setOnItemClickListener( this);
        // registramos aqui o novo menu de contexto
        registerForContextMenu(getListView());


       this.dialogConfirmacao = criaDialogConfirmacao();
    }




    //classe responsalver por classificar  por data e cor
    private class GastoViewBinder implements SimpleAdapter.ViewBinder {


        @Override
        public boolean setViewValue(View view, Object data,  String textRepresentation) {
            if(view.getId() == R.id.data && view.getId() == R.id.barraProgresso)  {
                if(!dataAnterior.equals(data)){
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    dataAnterior = textRepresentation;
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
                return true;
            }
            if(view.getId() == R.id.categoria){
                Integer id = (Integer) data;
                view.setBackgroundColor(getResources().getColor(id));
                return true;
            }
            return false;
        }
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = gastos.get(position);
        String descricao = (String) map.get("descricao");
        String mensagem = "Gasto selecionada: " + descricao;
        dialogConfirmacao.show();
        Toast.makeText(this, mensagem,Toast.LENGTH_SHORT).show();

        }

    //exibir menu gasto(nao funcioa por causa da heranca listView)
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,  ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasto_menu, menu);
    }
    //pegar objeto da listagem(nao funciona por causa da heranca lisview)
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remover) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            gastos.remove(info.position);
            getListView().invalidateViews();
            dataAnterior = "";
            // remover do banco de dados
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private AlertDialog criaDialogConfirmacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_gasto);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao),  this);
        return builder.create();

        }


    @Override
    public void onClick(DialogInterface dialog, int item){
        switch (item) {
        case 0:
             dialogConfirmacao.show();
             break;
        case DialogInterface.BUTTON_POSITIVE:
            gastos.remove(gastoselecionado);
            getListView().invalidateViews();
        break;
            case DialogInterface.BUTTON_NEGATIVE:
            dialogConfirmacao.dismiss();
        break;
    }


    }





}



