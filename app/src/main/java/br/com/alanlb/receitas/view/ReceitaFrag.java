package br.com.alanlb.receitas.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.alanlb.receitas.R;
import br.com.alanlb.receitas.dao.sqlite.ScriptsSQL;

public class ReceitaFrag extends Fragment {
    private TextView idText;
    private TextView nomeText;
    private TextView ingredientesText;
    private TextView modoDePreparoText;

    private int id;
    private String nome;
    private String ingredientes;
    private String modoDePreparo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receita_layout_frag, null);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getInt("ID", 10);
            nome = bundle.getString("NOME", "ALAN");
            ingredientes = bundle.getString("INGREDIENTES", "LIMEIRA");
            modoDePreparo = bundle.getString("MODODEPREPARO", "BRITO");
        }

        idText = view.findViewById(R.id.idTextFrag);
        nomeText = view.findViewById(R.id.nomeTextFrag);
        ingredientesText = view.findViewById(R.id.ingredientesTextFrag);
        modoDePreparoText = view.findViewById(R.id.modoDePreparoTextFrag);
        this.idText.setText(""+id);
        this.nomeText.setText(nome);
        this.ingredientesText.setText(ingredientes);
        this.modoDePreparoText.setText(modoDePreparo);

        return view;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
