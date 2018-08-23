package br.com.alanlb.receitas.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.MessageFormat;

import br.com.alanlb.receitas.dao.sqlite.FactorySQL;
import br.com.alanlb.receitas.dao.sqlite.ScriptsSQL;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Usuario;
import br.com.alanlb.receitas.util.Util;

public class UsuarioDAOSqlite extends UsuarioDAO{

    private SQLiteDatabase db;
    private Cursor cursor;
    private Usuario usuario;
    private AbstractFactoryDAO factoryDAO;
    private String sql;

    public UsuarioDAOSqlite(){
        factoryDAO = SingletonFactory.getFactory();
    }

    public void pegarBD(Context context) throws SqliteException {
        db = (SQLiteDatabase)factoryDAO.getBD(context);
    }

    @Override
    public void salvarUsuario(Context context, Usuario usuario) throws SqliteException {

        System.out.println("ENTROU.....");

        try {
            pegarBD(context);
            //deletarTabela(context);
            //db.execSQL(ScriptsSQL.Usuario.createTable);
            sql = ScriptsSQL.Usuario.insert;
            sql = MessageFormat.format(sql, usuario.getToSqlInsert());
            db.execSQL(sql);
            FactorySQL.closedb(context);


        } catch (SqliteException e) {
            e.printStackTrace();
            throw new SqliteException("Erro Insert Usuario");
        }

    }
    @Override
    public Usuario buscarUsuarioPorLogin(Context context, String login, String senha) throws SqliteException {
        pegarBD(context);
        login = "'" + login + "'";
        senha = "'" + senha + "'";
        sql = ScriptsSQL.Usuario.selectPorlogin;
        sql = MessageFormat.format(sql, login, senha);
        System.out.println(login);
        System.out.println(senha);
        System.out.println(sql);
        cursor = db.rawQuery(sql, null);
//            cursor = db.rawQuery("select * from usuario where login = ? ", new String[] {login});
//            cursor = db.query("usuario",null,"login=", new String[] {login}, null, null, null);

        if (cursor.moveToFirst()) {
            usuario = new Usuario();

            usuario.setId(cursor.getInt(cursor.getColumnIndex(ScriptsSQL.Usuario.columID)));
            usuario.setNomeCompleto(cursor.getString(cursor.getColumnIndex("nome_completo")));
            usuario.setLogin(cursor.getString(cursor.getColumnIndex("login")));
            usuario.setSenha(cursor.getString(cursor.getColumnIndex("senha")));
            usuario.setSincronizado(Boolean.valueOf(cursor.getString(cursor.getColumnIndex("sincronizado"))));
            usuario.setDataCadastro(Util.getDateToString(cursor.getString(cursor.getColumnIndex("data_cadatro")), Util.dataTimeFormater_dd_MM_yyy_HH_mm_ss));
            usuario.setUltimoAcesso(Util.getDateToString(cursor.getString(cursor.getColumnIndex("ultimo_acesso")), Util.dataTimeFormater_dd_MM_yyy_HH_mm_ss));
            FactorySQL.closedb(context);
            return usuario;
        }
        throw new SqliteException("usuario ou senha inválida");

    }
    @Override
    public Usuario buscarUsuarioPorId(Context context, int id) throws SqliteException {
        pegarBD(context);
        //id = "'" + id + "'";
        sql = ScriptsSQL.Usuario.selectPorId;
        sql = MessageFormat.format(sql, id);
        System.out.println(sql);
        cursor = db.rawQuery(sql, null);
//            cursor = db.rawQuery("select * from usuario where login = ? ", new String[] {login});
//            cursor = db.query("usuario",,"login=", new String[] {login}, null, null, null);
//            cursor = db.query("usuario", new String[]{"id"}, "id" + "= ? ", new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null) {
            if (cursor.moveToFirst()) {
                usuario = new Usuario();

                usuario.setId(cursor.getInt(cursor.getColumnIndex(ScriptsSQL.Usuario.columID)));
                usuario.setIdFireBase(cursor.getString(cursor.getColumnIndex("id_firebase")));
                usuario.setNomeCompleto(cursor.getString(cursor.getColumnIndex("nome_completo")));
                usuario.setLogin(cursor.getString(cursor.getColumnIndex("login")));
                usuario.setSenha(cursor.getString(cursor.getColumnIndex("senha")));
                usuario.setSincronizado(Boolean.valueOf(cursor.getString(cursor.getColumnIndex("sincronizado"))));
                usuario.setDataCadastro(Util.getDateToString(cursor.getString(cursor.getColumnIndex("data_cadatro")), Util.dataTimeFormater_dd_MM_yyy_HH_mm_ss));
                usuario.setUltimoAcesso(Util.getDateToString(cursor.getString(cursor.getColumnIndex("ultimo_acesso")), Util.dataTimeFormater_dd_MM_yyy_HH_mm_ss));
                FactorySQL.closedb(context);
                return usuario;
            }
        }
        throw new SqliteException("usuario ou senha inválida");

    }
    @Override
    public Boolean isUsuarioLogado(Context context) throws SqliteException {

        pegarBD(context);
        sql = ScriptsSQL.Usuario.selectAll;
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        return Boolean.valueOf(cursor.getString(cursor.getColumnIndex("sincronizado")));

    }
    @Override
    public void deletarTabela(Context context) throws SqliteException {
        try {
            pegarBD(context);
            sql = ScriptsSQL.Usuario.dropTable;
            db.execSQL(sql);
            FactorySQL.closedb(context);

        } catch (SqliteException e) {
            e.printStackTrace();
            throw new SqliteException("Erro Insert Usuario");
        }

    }
    @Override
    public void deletarTabelaPorId(Context context, int id) throws SqliteException {
        try {
            pegarBD(context);
            sql = ScriptsSQL.Usuario.deleteTableById;
            sql = MessageFormat.format(sql, id);
            db.execSQL(sql);
            FactorySQL.closedb(context);

        } catch (SqliteException e) {
            e.printStackTrace();
            throw new SqliteException("Erro Insert Usuario");
        }

    }
}
