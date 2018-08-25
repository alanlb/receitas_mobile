package br.com.alanlb.receitas.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import br.com.alanlb.receitas.dao.sqlite.FactorySQL;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.exception.ValidationException;
import br.com.alanlb.receitas.model.Receita;
import br.com.alanlb.receitas.model.Usuario;
import br.com.alanlb.receitas.util.Util;

public class Facade {

    private static UsuarioDAO usuarioDAO = SingletonFactory.getFactory().getUsuarioDAO();
    private static ReceitaDAO receitaDAO = SingletonFactory.getFactory().getReceitaDAO();
    //Métodos de usuário

    public static void cadastrarUsuario(Context context, Usuario usuario) throws SqliteException {
        usuarioDAO.salvarUsuario(context, usuario);
    }

    public static void isLogadoUsuario(Context context) throws SqliteException {
        usuarioDAO.isUsuarioLogado(context);
    }

    public static Usuario buscarUsuarioPorID(Context context, int id) throws SqliteException {
        return usuarioDAO.buscarUsuarioPorId(context, id);
    }

    public static Usuario buscarUsuarioPorLogin(Context context, String login, String senha) throws SqliteException {
        return usuarioDAO.buscarUsuarioPorLogin(context, login, senha);
    }

    public static void verificarSeUsuarioExiste(Context context, String login, String senha) throws SqliteException {
        usuarioDAO.buscarUsuarioPorLogin(context, login, senha);
    }

        //Apagar Tabela Usuario

    public static void deletarTabelaUsuario(Context context) throws SqliteException {
        usuarioDAO.deletarTabela(context);
    }

    public static void deletarTabelaPorId(Context context, int id) throws SqliteException {
        usuarioDAO.deletarTabelaPorId(context, id);
    }
    //Métodos de Receita

    public static void criarTabelaReceita(Context context) throws SqliteException {
        receitaDAO.criarTabela(context);
    }

    public static void cadastrarReceita(Context context, Receita receita) throws SqliteException {
        receitaDAO.salvarReceita(context, receita);
    }

    public static ArrayList<Receita> buscarReceitaPorNome(Context context, String nome) throws SqliteException {
        return receitaDAO.buscarReceitaPorNome(context, nome);
    }

    public static ArrayList<Receita> buscarReceitaPorUsuario(Context context, String idUsuario) throws SqliteException {
        return receitaDAO.buscarReceitaPorUsuario(context, idUsuario);
    }

    public static void deletarReceita(Context context, int id) throws SqliteException {
        receitaDAO.deletarReceita(context, id);
    }

    public static boolean isReceitaSincronizada(Context context, String idFirebase) throws SqliteException {
        return receitaDAO.buscarReceitaPorIdFirebase(context, idFirebase);
    }

        //Apagar Tabela Receita

    public static void deletarTabelaReceita(Context context) throws SqliteException {
        receitaDAO.deletarTabela(context);
    }

    //Banco de dados

    public static void closeDB(Context context) throws SqliteException {
        FactorySQL.closedb(context);
    }

    //Util
    public static void validarEmailUsuario(String email) throws ValidationException {
        Util.validarEmail(email);
    }

    public static void validarSenha(String senha) throws ValidationException {
        Util.validaSenha(senha);
    }

    public static void validarCadastro(String nome, String login, String senha) throws ValidationException {
        Util.validarLoginCadastroLogin(nome, login, senha);
    }

    public static String formatarData(Date date, String formato){
        return Util.getStringToFormaterDate(date, formato);
    }
}
