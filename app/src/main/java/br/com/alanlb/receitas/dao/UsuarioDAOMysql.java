package br.com.alanlb.receitas.dao;

import android.content.Context;

import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Usuario;

public class UsuarioDAOMysql extends UsuarioDAO {
    @Override
    public void salvarUsuario(Context context, Usuario usuario) throws SqliteException {

    }

    @Override
    public Usuario buscarUsuarioPorLogin(Context context, String login, String senha) throws SqliteException {
        return null;
    }

    @Override
    public Usuario buscarUsuarioPorId(Context context, int id) throws SqliteException {
        return null;
    }

    @Override
    public Boolean isUsuarioLogado(Context context) throws SqliteException {
        return null;
    }

    @Override
    public void deletarTabela(Context context) throws SqliteException {

    }
}
