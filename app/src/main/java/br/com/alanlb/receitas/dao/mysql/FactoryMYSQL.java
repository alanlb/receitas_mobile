package br.com.alanlb.receitas.dao.mysql;

import android.content.Context;

import br.com.alanlb.receitas.dao.AbstractFactoryDAO;
import br.com.alanlb.receitas.dao.ReceitaDAO;
import br.com.alanlb.receitas.dao.UsuarioDAO;
import br.com.alanlb.receitas.dao.UsuarioDAOMysql;
import br.com.alanlb.receitas.exception.SqliteException;

public class FactoryMYSQL extends AbstractFactoryDAO {
    @Override
    public Object getBD(Context context) throws SqliteException {
        return null;
    }

    @Override
    public UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOMysql();
    }

    @Override
    public ReceitaDAO getReceitaDAO() {
        return null;
    }
}
