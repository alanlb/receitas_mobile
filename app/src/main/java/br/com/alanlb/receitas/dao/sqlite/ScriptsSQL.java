package br.com.alanlb.receitas.dao.sqlite;

public class ScriptsSQL {


    public static class Usuario {
        public static final String columID  = "id";

        public static final String createTable = "" +
                "CREATE TABLE usuario(id integer primary key, " +
                "id_firebase varchar not null, " +
                "nome_completo varchar not null, " +
                "login varchar not null unique, " +
                "senha varchar not null, " +
                "sincronizado boolean not null,"+
                "data_cadatro datetime not null, " +
                "ultimo_acesso datetime not null);";
        public static final String dropTable = "DROP TABLE IF EXISTS usuario";

        public static String insert = "" +
                "INSERT INTO usuario(" +
                columID+","+
                "id_firebase," +
                "nome_completo," +
                "login," +
                "senha," +
                "sincronizado," +
                "data_cadatro," +
                "ultimo_acesso)" +
                "values ({0})";

        public static String selectPorlogin = "select * from usuario where login = {0} and senha={1}";
        public static String selectPorId = "select * from usuario where id = {0}";
        public static String selectAll = "select * from usuario";
        public static String deleteTableById = "delete from usuario where id = {0}";
    }

    public static class Receita {
        public static final String columID  = "id";

        public static final String createTable =  "" +
                "CREATE TABLE receita(id integer primary key AUTOINCREMENT, " +
                "id_firebase varchar not null, " +
                "nome varchar not null, " +
                "ingredientes varchar not null, " +
                "modo_de_preparo varchar not null, " +
                "id_usuario varchar not null," +
                "path_img varchar not null, " +
                "url_img varchar not null); ";



        public static final String insert = "" +
                "INSERT INTO receita(" +
                //"id"+","+
                "id_firebase," +
                "nome," +
                "ingredientes," +
                "modo_de_preparo," +
                "id_usuario," +
                "path_img," +
                "url_img)" +
                "values ({0})";
        public static String update = "";
        public static final String dropTable = "DROP TABLE IF EXISTS receita";
        public static final String dropReceita = "DELETE FROM receita where id = {0}";

        public static final String selectPorNome = "select * from receita where nome LIKE {0}";
        public static final String selectPorUsuario = "select * from receita where id_usuario = {0}";
        public static String selectAll = "select * from receita";
    }
}
