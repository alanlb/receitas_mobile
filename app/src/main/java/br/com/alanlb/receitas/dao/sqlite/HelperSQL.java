package br.com.alanlb.receitas.dao.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperSQL extends SQLiteOpenHelper{

    private static final String NOME = "receitas.sqlite";
    private static final int VERSION = 7;
    private final String log = "DB";
    private Context context;

    public HelperSQL(Context context) {
        super(context, NOME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("onCreate - SQLITE");
        criarTabelas(db);
    }

    private void criarTabelas(SQLiteDatabase db) {
        db.execSQL(ScriptsSQL.Usuario.createTable);
        db.execSQL(ScriptsSQL.Receita.createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("onUpgrade - SQLITE");
        db.execSQL(ScriptsSQL.Usuario.dropTable);
        db.execSQL(ScriptsSQL.Receita.dropTable);
        onCreate(db);

    }
}
