package br.com.alanlb.receitas.dao;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import br.com.alanlb.receitas.dao.firebase.FireBaseBD;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Receita;

public class ReceitaDAOFireBase{



    public static void salvarReceita(Context context, Receita receita) throws SqliteException {
        DatabaseReference reference = FireBaseBD.getReference(context);
        reference.child("Receita").push().setValue(receita);
    }

    public ArrayList<Receita> buscarReceitaPorNome(Context context, String nome) throws SqliteException {
        return null;
    }

    public List<Receita> buscarToasReceitas(Context context) throws SqliteException {
        return null;
    }

    public ArrayList<Receita> buscarReceitaPorUsuario(Context context, int id) throws SqliteException {
        return null;
    }

    public void deletarReceita(Context context, int id) throws SqliteException {

    }

    public void deletarTabela(Context context) throws SqliteException {

    }
}
