package br.com.alanlb.receitas.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.alanlb.receitas.R;
import br.com.alanlb.receitas.dao.Facade;
import br.com.alanlb.receitas.dao.ReceitaDAOFireBase;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Receita;

public class ReceitaFrag extends Fragment {
    private TextView idText;
    private EditText nomeText;
    private EditText ingredientesText;
    private EditText modoDePreparoText;
    private Button buttonEditar;
    private Button buttonSalvarEditavel;

    private int id;
    private String idFirebase;
    private String nome;
    private String ingredientes;
    private String modoDePreparo;
    private String idUsuario;
    private String pathImg;
    private String url;
    private boolean proprioDonoReceitaUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.receita_layout_frag, null);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getInt("ID", 10);
            idFirebase = bundle.getString("IDFIREBASE");
            nome = bundle.getString("NOME", "ALAN");
            ingredientes = bundle.getString("INGREDIENTES", "LIMEIRA");
            modoDePreparo = bundle.getString("MODODEPREPARO", "BRITO");
            idUsuario = bundle.getString("IDUSUARIO");
            pathImg = bundle.getString("PATHIMG");
            url = bundle.getString("URL");
            proprioDonoReceitaUsuario = bundle.getBoolean("DONO");
        }


        idText = view.findViewById(R.id.idTextFrag);
        nomeText = view.findViewById(R.id.nomeTextFrag);
        ingredientesText = view.findViewById(R.id.ingredientesTextFrag);
        modoDePreparoText = view.findViewById(R.id.modoDePreparoTextFrag);
        this.idText.setText(""+id);
        this.nomeText.setText(nome);
        this.ingredientesText.setText(ingredientes);
        this.modoDePreparoText.setText(modoDePreparo);

        if(proprioDonoReceitaUsuario){
            buttonEditar = view.findViewById(R.id.buttoneditar);
            buttonEditar.setVisibility(View.VISIBLE);
            buttonEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nomeText.setEnabled(true);
                    ingredientesText.setEnabled(true);
                    modoDePreparoText.setEnabled(true);

                    nomeText.setBackgroundColor(Color.WHITE);
                    ingredientesText.setBackgroundColor(Color.WHITE);
                    modoDePreparoText.setBackgroundColor(Color.WHITE);

                    nomeText.setTextColor(Color.rgb(0,128,128));
                    ingredientesText.setTextColor(Color.rgb(0,128,128));
                    modoDePreparoText.setTextColor(Color.rgb(0,128,128));

                    nomeText.requestFocus();
                    buttonSalvarEditavel.setVisibility(View.VISIBLE);
                }
            });


            buttonSalvarEditavel = view.findViewById(R.id.buttonsalvareditavel);
            buttonSalvarEditavel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        //##################### NOVA RECEITA ###########
                        final Receita receita = new Receita();
                        receita.setId(id);
                        receita.setIdFireBase(idFirebase);
                        receita.setNome(nomeText.getText().toString());
                        receita.setIngredientes(ingredientesText.getText().toString());
                        receita.setModoDePreparo(modoDePreparoText.getText().toString());
                        receita.setId_usuario(idUsuario);
                        receita.setPathImg(pathImg);
                        receita.setUrl(url);
                        //##############################################
                        ReceitaDAOFireBase.atualizarReceitaFirebase(view.getContext(), receita);

                        nomeText.setEnabled(false);
                        ingredientesText.setEnabled(false);
                        modoDePreparoText.setEnabled(false);

                        nomeText.setTextColor(Color.rgb(51,181,229));
                        ingredientesText.setTextColor(Color.rgb(170,170,170));
                        modoDePreparoText.setTextColor(Color.rgb(170,170,170));

                        buttonSalvarEditavel.setVisibility(View.INVISIBLE);

                    } catch (SqliteException e) {
                        Toast.makeText(view.getContext(),"Não foi possível atualizar", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        }else{
            buttonEditar = view.findViewById(R.id.buttoneditar);
            buttonEditar.setVisibility(View.VISIBLE);
            buttonEditar.setText("Salvar");
            buttonEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        //##################### NOVA RECEITA ###########
                        final Receita receita = new Receita();
                        receita.setId(id);
                        receita.setIdFireBase(idFirebase);
                        receita.setNome(nomeText.getText().toString());
                        receita.setIngredientes(ingredientesText.getText().toString());
                        receita.setModoDePreparo(modoDePreparoText.getText().toString());
                        receita.setId_usuario(Facade.buscarUsuarioPorID(view.getContext(), 1).getIdFireBase());
                        receita.setPathImg(pathImg);
                        receita.setUrl(url);
                        //##############################################

                        ReceitaDAOFireBase.salvarReceita(view.getContext(), receita);
                        buttonEditar.setVisibility(View.INVISIBLE);
                        Toast.makeText(view.getContext(), "Salvo com sucesso!",Toast.LENGTH_LONG).show();
                    } catch (SqliteException e) {
                        Toast.makeText(view.getContext(),"Não foi possível atualizar", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

        }
        return view;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
