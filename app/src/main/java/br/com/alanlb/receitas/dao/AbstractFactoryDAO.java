package br.com.alanlb.receitas.dao;

import android.content.Context;

import br.com.alanlb.receitas.dao.mysql.FactoryMYSQL;
import br.com.alanlb.receitas.dao.sqlite.FactorySQL;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.SGBD;

public abstract class AbstractFactoryDAO {
    private static  AbstractFactoryDAO factory;

    public synchronized static AbstractFactoryDAO getInstance(SGBD sgbd){
        if (factory == null) {
            switch (sgbd) {
                case SQLITE:
                    factory = new FactorySQL();
                    return factory;
                case MYSQL:
                    factory = new FactoryMYSQL();
                    return factory;
            }
        }
        return factory;
    }

    public abstract Object getBD(Context context) throws SqliteException;

    public abstract UsuarioDAO getUsuarioDAO();

    public abstract ReceitaDAO getReceitaDAO();

}
