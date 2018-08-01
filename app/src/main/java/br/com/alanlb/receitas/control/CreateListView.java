package br.com.alanlb.receitas.control;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.alanlb.receitas.MainActivity;
import br.com.alanlb.receitas.R;
import br.com.alanlb.receitas.dao.ReceitaDAO;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Item;
import br.com.alanlb.receitas.model.Receita;
import br.com.alanlb.receitas.util.ListAdapterItem;
import br.com.alanlb.receitas.view.AdicionarReceitasFragment;
import br.com.alanlb.receitas.view.MinhasReceitasFragment;
import br.com.alanlb.receitas.view.ReceitaFrag;

public class CreateListView implements AdapterView.OnItemClickListener {
    private Context context;
    private ListView listView;
    private int id;
    private ArrayList<Receita> receitas;
    private Dialog dialog;

    public CreateListView(Context context, ListView listView, int id){
        this.context = context;
        this.listView = listView;
        this.id = id;

        criar(context, listView, id);
    }

    public CreateListView(Context context, ArrayList<Receita> lista, Dialog dialog){
         this.receitas = lista;
         this.context = context;
         this.dialog = dialog;
    }

    public void criar(Context context, ListView listView, int id){
        try {
            ArrayList<Receita> receitas = ReceitaDAO.buscarReceitaPorUsuario(context, id);
            this.receitas = receitas;
            ArrayList<Item> itens = new ArrayList<Item>();

            for(Receita receita: receitas){
                itens.add(new Item(receita.getNome(),receita.getId()));
            }

            ListAdapterItem adapter = new ListAdapterItem(context , itens);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(null);
            listView.setOnItemClickListener(this);
        } catch (SqliteException e) {
            new SqliteException("Não foi possível achar a receita");
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> lv, View view, int position, long id) {
        System.out.println("Receita nome: " + this.receitas.get(position).getNome() +
                            "Id: " + this.receitas.get(position).getId() +
                            "Receita: " + this.receitas.get(position).getIngredientes());

        Bundle bundle = new Bundle();
        bundle.putInt("ID",receitas.get(position).getId());
        bundle.putString("NOME",receitas.get(position).getNome());
        bundle.putString("INGREDIENTES", receitas.get(position).getIngredientes());
        bundle.putString("MODODEPREPARO", receitas.get(position).getModoDePreparo());

        ReceitaFrag frag = new ReceitaFrag();
        FragmentManager fm = ((MainActivity)this.context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        frag.setArguments(bundle);
        ft.replace(R.id.layoutparafragmentos, frag,"receita_frag");
        ft.commit();

        if(dialog != null){
            dialog.cancel();
        }
    }
}
