package br.com.alanlb.receitas.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Receita;

public class ReceitaDAOMysql extends ReceitaDAO {
    @Override
    public void salvarReceita(Context context, Receita receita) throws SqliteException {

    }

    @Override
    public ArrayList<Receita> buscarReceitaPorNome(Context context, String nome) throws SqliteException {
        return null;
    }

    @Override
    public List<Receita> buscarToasReceitas(Context context) throws SqliteException {
        return null;
    }

    @Override
    public ArrayList<Receita> buscarReceitaPorUsuario(Context context, int id) throws SqliteException {
        return null;
    }

    @Override
    public void deletarReceita(Context context, int id) throws SqliteException {

    }

    @Override
    public void deletarTabela(Context context) throws SqliteException {

    }
}
