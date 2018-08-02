package br.com.alanlb.receitas.dao.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.com.alanlb.receitas.dao.AbstractFactoryDAO;
import br.com.alanlb.receitas.dao.ReceitaDAO;
import br.com.alanlb.receitas.dao.ReceitaDAOSqlite;
import br.com.alanlb.receitas.dao.UsuarioDAO;
import br.com.alanlb.receitas.dao.UsuarioDAOSqlite;
import br.com.alanlb.receitas.exception.SqliteException;

public class FactorySQL extends AbstractFactoryDAO{

    private static SQLiteDatabase db;
    private static HelperSQL helper;


    public FactorySQL() {

    }

    @Override
    public Object getBD(Context context) throws SqliteException {
        try {
            if (db == null || !db.isOpen()) {
                helper = new HelperSQL(context);
                db = helper.getWritableDatabase();
            }

        } catch (Exception e) {
            throw new SqliteException("SQLITE Exception");
        }
        return db;
    }

    public static void closedb(Context context) throws SqliteException {
        try {
            if (db != null || db.isOpen()) {
                db.close();
                if (helper != null) helper.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new SqliteException("Erro ClosedDb");
        }
    }

    public static void closeDB(Context context) throws SqliteException {
        try {
            if (db != null || db.isOpen()) {
                db.close();
                if (helper != null) helper.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new SqliteException("Erro ClosedDb");
        }
    }

    @Override
    public UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOSqlite();
    }

    @Override
    public ReceitaDAO getReceitaDAO() {
        return new ReceitaDAOSqlite();
    }
}
