package br.com.alanlb.receitas.dao;

import android.content.Context;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Usuario;

public abstract class UsuarioDAO {

    public abstract void salvarUsuario(Context context, Usuario usuario) throws SqliteException;

    public abstract Usuario buscarUsuarioPorLogin(Context context, String login, String senha) throws SqliteException;

    public abstract Usuario buscarUsuarioPorId(Context context, int id) throws SqliteException;

    public abstract Boolean isUsuarioLogado(Context context) throws SqliteException;

    public abstract void deletarTabela(Context context) throws SqliteException;
}