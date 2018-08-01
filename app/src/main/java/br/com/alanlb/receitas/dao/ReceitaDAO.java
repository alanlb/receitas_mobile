package br.com.alanlb.receitas.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.alanlb.receitas.dao.sqlite.FactorySQL;
import br.com.alanlb.receitas.dao.sqlite.ScriptsSQL;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Receita;

public class ReceitaDAO {
    private static SQLiteDatabase db;
    private static Cursor cursor;
    private static Receita receita;

    private static String sql;

    public static void salvarReceita(Context context, Receita receita) throws SqliteException {

        System.out.println("ENTROU.....");

        try {
            db = FactorySQL.getInstance(context);
            sql = ScriptsSQL.Receita.insert;
            sql = MessageFormat.format(sql, receita.getToSqlInsert());
            db.execSQL(sql);
            FactorySQL.closedb(context);

        } catch (SqliteException e) {
            e.printStackTrace();
            throw new SqliteException("Erro Insert Receita");
        }

    }

    public static ArrayList<Receita> buscarReceitaPorNome(Context context, String nome) throws SqliteException {
        db = FactorySQL.getInstance(context);
        nome = "'%" + nome + "%'";
        sql = ScriptsSQL.Receita.selectPorNome;
        sql = MessageFormat.format(sql, nome);
        ArrayList<Receita> receitas = new ArrayList<Receita>();
        cursor = db.rawQuery(sql, null);
//            cursor = db.rawQuery("select * from usuario where login = ? ", new String[] {login});
//            cursor = db.query("usuario",null,"login=", new String[] {login}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                receita = new Receita();
                String id_receita = cursor.getString(cursor.getColumnIndex("id"));
                String id_usuario = cursor.getString(cursor.getColumnIndex("id_usuario"));
                receita.setId(Integer.parseInt(id_receita));
                receita.setId_usuario(Integer.parseInt(id_usuario));
                receita.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                receita.setIngredientes(cursor.getString(cursor.getColumnIndex("ingredientes")));
                receita.setModoDePreparo(cursor.getString(cursor.getColumnIndex("modo_de_preparo")));
                receitas.add(receita);
                System.out.println(id_receita);
                cursor.moveToNext();
            }
            cursor.close();
        }
        FactorySQL.closedb(context);
        return receitas;
    }



    public static List<Receita> buscarToasReceitas(Context context) throws SqliteException {

        db = FactorySQL.getInstance(context);
        sql = ScriptsSQL.Receita.selectAll;
        cursor = db.rawQuery(sql, null);
        ArrayList<Receita> receitas = new ArrayList<Receita>();
        if (cursor.moveToFirst()) {
            receita = new Receita();
            String id = cursor.getString(cursor.getColumnIndex("id"));
            receita.setId(Integer.parseInt(id));
            receita.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            receita.setIngredientes(cursor.getString(cursor.getColumnIndex("ingredientes")));
            FactorySQL.closedb(context);
            receitas.add(receita);
            throw new SqliteException("Pau ao buscar todas as receitas");
        }

        return receitas;

    }

    public static ArrayList<Receita> buscarReceitaPorUsuario(Context context, int id) throws SqliteException {
        db = FactorySQL.getInstance(context);
//        nome = "'" + nome + "'";
        sql = ScriptsSQL.Receita.selectPorUsuario;
        sql = MessageFormat.format(sql, id);
        cursor = db.rawQuery(sql, null);
//            cursor = db.rawQuery("select * from usuario where login = ? ", new String[] {login});
//            cursor = db.query("usuario",null,"login=", new String[] {login}, null, null, null);
        ArrayList<Receita> receitas = new ArrayList<Receita>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                receita = new Receita();
                String id_receita = cursor.getString(cursor.getColumnIndex("id"));
                String id_usuario = cursor.getString(cursor.getColumnIndex("id_usuario"));
                receita.setId(Integer.parseInt(id_receita));
                receita.setId_usuario(Integer.parseInt(id_usuario));
                receita.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                receita.setIngredientes(cursor.getString(cursor.getColumnIndex("ingredientes")));
                receita.setModoDePreparo(cursor.getString(cursor.getColumnIndex("modo_de_preparo")));
                receitas.add(receita);
                System.out.println(id_receita);
                cursor.moveToNext();
            }
            cursor.close();
        }
        FactorySQL.closedb(context);
        return receitas;


    }

    public static void deletarReceita(Context context, int id)throws SqliteException{
        db = FactorySQL.getInstance(context);
        db.delete("receita", "id = "+id, null);
//        nome = "'" + nome + "'";
//        sql = ScriptsSQL.Receita.dropReceita;
//        sql = MessageFormat.format(sql, id);
//        System.err.println("SQL --> "+sql);
//        db.rawQuery(sql, null);
//        FactorySQL.closedb(context);
    }
}