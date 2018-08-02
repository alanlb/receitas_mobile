package br.com.alanlb.receitas.dao;

import android.content.Context;

import br.com.alanlb.receitas.dao.mysql.FactoryMYSQL;
import br.com.alanlb.receitas.dao.sqlite.FactorySQL;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.SGBD;

public abstract class AbstractFactoryDAO {


    public static AbstractFactoryDAO getInstance(SGBD sgbd){
        switch (sgbd){
            case SQLITE:
                return new FactorySQL();
            case MYSQL:
                return new FactoryMYSQL();
        }
        return null;
    }

    public abstract Object getBD(Context context) throws SqliteException;

    public abstract UsuarioDAO getUsuarioDAO();

    public abstract ReceitaDAO getReceitaDAO();

}
