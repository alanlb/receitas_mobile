package br.com.alanlb.receitas.control;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.alanlb.receitas.MainActivity;
import br.com.alanlb.receitas.R;
import br.com.alanlb.receitas.dao.ReceitaDAO;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Receita;
import br.com.alanlb.receitas.view.PrincipalFragment;
import br.com.alanlb.receitas.view.ReceitaFrag;

public class DropListener implements AdapterView.OnItemClickListener {
    private ArrayList<Receita> receitas;
    private Context context;
    private Dialog dialog;

    public DropListener(Context context, ArrayList<Receita> lista, Dialog dialog){
        this.receitas = lista;
        this.context = context;
        this.dialog = dialog;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("Receita nome: " + this.receitas.get(i).getNome() +
                "Id: " + this.receitas.get(i).getId() +
                "Receita: " + this.receitas.get(i).getIngredientes());

        try {
            ReceitaDAO.deletarReceita(this.context, receitas.get(i).getId());
        } catch (SqliteException e) {
            Toast.makeText(this.context, "Falha ao tentar apagar Receita", Toast.LENGTH_SHORT).show();
        }

        PrincipalFragment frag = new PrincipalFragment();
        FragmentManager fm = ((MainActivity)this.context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.layoutparafragmentos, frag,"receita_frag");
        ft.commit();

        if(dialog != null){
            dialog.cancel();
        }
    }
}
