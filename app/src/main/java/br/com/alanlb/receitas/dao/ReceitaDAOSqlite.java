package br.com.alanlb.receitas.dao;

import android.content.ContentValues;
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

public class ReceitaDAOSqlite extends ReceitaDAO{
    private static SQLiteDatabase db;
    private static Cursor cursor;
    private static Receita receita;
    private AbstractFactoryDAO factoryDAO;

    private static String sql;
    public ReceitaDAOSqlite(){
        factoryDAO = SingletonFactory.getFactory();
    }

    public void pegaBD(Context context) throws SqliteException {
        db = (SQLiteDatabase)factoryDAO.getBD(context);
    }

    @Override
    public void criarTabela(Context context) throws SqliteException {
        try {
            pegaBD(context);
            db.execSQL(ScriptsSQL.Receita.createTable);
        } catch (SqliteException e) {
            e.printStackTrace();
            throw new SqliteException("Erro Criar tabela");
        }

    }

    @Override
    public void salvarReceita(Context context, Receita receita) throws SqliteException {

        System.out.println("ENTROU.....");

        try {
            pegaBD(context);
            sql = ScriptsSQL.Receita.insert;
            sql = MessageFormat.format(sql, receita.getToSqlInsert());
            db.execSQL(sql);
            FactorySQL.closedb(context);
            System.out.println("SQL --------- "+sql);

        } catch (SqliteException e) {
            e.printStackTrace();
            throw new SqliteException("Erro Insert Receita");
        }

    }



    @Override
    public ArrayList<Receita> buscarReceitaPorNome(Context context, String nome) throws SqliteException {
        pegaBD(context);
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
                receita.setId_usuario(id_usuario);
                receita.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                receita.setIngredientes(cursor.getString(cursor.getColumnIndex("ingredientes")));
                receita.setModoDePreparo(cursor.getString(cursor.getColumnIndex("modo_de_preparo")));
                receita.setPathImg(cursor.getString(cursor.getColumnIndex("path_img")));
                receita.setUrl(cursor.getString(cursor.getColumnIndex("url_img")));
                receita.setIdFireBase(cursor.getString(cursor.getColumnIndex("id_firebase")));
                receitas.add(receita);
                cursor.moveToNext();
            }
            cursor.close();
        }
        FactorySQL.closedb(context);
        return receitas;
    }

    @Override
    public boolean buscarReceitaPorIdFirebase(Context context, String id) throws SqliteException {
        pegaBD(context);
        Cursor cursor;
        String[] campos = {"id_firebase"};
        cursor = db.query("receita", campos,null,null,null,null,null);

        System.out.println("BUSCANDOOOOOOOOOOO");
        if(cursor != null){
            System.out.println("CURSOR NAO E NULO");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String id_firebase = cursor.getString(cursor.getColumnIndex("id_firebase"));
                System.out.println("TESTANTOOOOO");
                System.out.println("TESTE  >>>>>>> "+ id_firebase + " : "+id);
                if (id_firebase.equals(id))
                    return true;
                cursor.moveToNext();
            }
        }
        return false;
    }


    @Override
    public List<Receita> buscarToasReceitas(Context context) throws SqliteException {

        pegaBD(context);
        sql = ScriptsSQL.Receita.selectAll;
        cursor = db.rawQuery(sql, null);
        ArrayList<Receita> receitas = new ArrayList<Receita>();
        if (cursor.moveToFirst()) {
            receita = new Receita();
            String id = cursor.getString(cursor.getColumnIndex("id"));
            receita.setId(Integer.parseInt(id));
            receita.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            receita.setIngredientes(cursor.getString(cursor.getColumnIndex("ingredientes")));
            receita.setModoDePreparo(cursor.getString(cursor.getColumnIndex("modo_de_preparo")));
            receita.setPathImg(cursor.getString(cursor.getColumnIndex("path_img")));
            receita.setUrl(cursor.getString(cursor.getColumnIndex("url_img")));
            receita.setIdFireBase(cursor.getString(cursor.getColumnIndex("id_firebase")));
            FactorySQL.closedb(context);
            receitas.add(receita);
            throw new SqliteException("Pau ao buscar todas as receitas");
        }

        return receitas;

    }
    @Override
    public ArrayList<Receita> buscarReceitaPorUsuario(Context context, String id) throws SqliteException {
        pegaBD(context);
        id = "'" + id + "'";
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
                receita.setId_usuario(id_usuario);
                receita.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                receita.setIngredientes(cursor.getString(cursor.getColumnIndex("ingredientes")));
                receita.setModoDePreparo(cursor.getString(cursor.getColumnIndex("modo_de_preparo")));
                receita.setPathImg(cursor.getString(cursor.getColumnIndex("path_img")));
                receita.setUrl(cursor.getString(cursor.getColumnIndex("url_img")));
                receita.setIdFireBase(cursor.getString(cursor.getColumnIndex("id_firebase")));
                receitas.add(receita);
                cursor.moveToNext();
            }
            cursor.close();
        }
        FactorySQL.closedb(context);
        return receitas;


    }
    @Override
    public void deletarReceita(Context context, int id)throws SqliteException{
        pegaBD(context);
        db.delete("receita", "id = "+id, null);
//        nome = "'" + nome + "'";
//        sql = ScriptsSQL.Receita.dropReceita;
//        sql = MessageFormat.format(sql, id);
//        System.err.println("SQL --> "+sql);
//        db.rawQuery(sql, null);
//        FactorySQL.closedb(context);
    }
    @Override
    public void deletarTabela(Context context) throws SqliteException {
        try {
            pegaBD(context);
            sql = ScriptsSQL.Receita.dropTable;
            db.execSQL(sql);
            FactorySQL.closedb(context);

        } catch (SqliteException e) {
            e.printStackTrace();
            throw new SqliteException("Erro Insert Usuario");
        }

    }

    @Override
    public void atualizaReceita(Context context, Receita receita, int id) throws SqliteException {
        pegaBD(context);
        ContentValues valores = new ContentValues();
        String where;

        where= "id = " +id;

        valores.put("nome", receita.getNome());
        valores.put("ingredientes", receita.getIngredientes());
        valores.put("modo_de_preparo", receita.getModoDePreparo());
        valores.put("id_usuario", receita.getId_usuario());
        valores.put("path_img", receita.getPathImg());
        valores.put("url_img", receita.getUrl());

        db.update("receita",valores, where, null);
        db.close();

//        "INSERT INTO receita(" +
//                //"id"+","+
//                "nome," +
//                "ingredientes," +
//                "modo_de_preparo," +
//                "id_usuario," +
//                "path_img," +
//                "url_img)" +
//                "values ({0})";
    }
}
