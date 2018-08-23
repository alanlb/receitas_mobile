package br.com.alanlb.receitas.dao;

import android.content.Context;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.alanlb.receitas.dao.sqlite.FactorySQL;
import br.com.alanlb.receitas.dao.sqlite.ScriptsSQL;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Receita;

public abstract class ReceitaDAO {

    public abstract void criarTabela(Context context) throws SqliteException;

    public abstract void salvarReceita(Context context, Receita receita) throws SqliteException;

    public abstract ArrayList<Receita> buscarReceitaPorNome(Context context, String nome) throws SqliteException;

    public abstract List<Receita> buscarToasReceitas(Context context) throws SqliteException;

    public abstract ArrayList<Receita> buscarReceitaPorUsuario(Context context, String id) throws SqliteException;

    public abstract void deletarReceita(Context context, int id)throws SqliteException;

    public abstract void deletarTabela(Context context) throws SqliteException;
}
