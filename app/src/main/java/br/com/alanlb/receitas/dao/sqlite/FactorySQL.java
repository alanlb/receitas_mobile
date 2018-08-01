package br.com.alanlb.receitas.dao.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.com.alanlb.receitas.exception.SqliteException;

public class FactorySQL {

    private static SQLiteDatabase db;
    private static HelperSQL helper;


    private FactorySQL() {

    }

    public static SQLiteDatabase getInstance(Context context) throws SqliteException {
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
}
