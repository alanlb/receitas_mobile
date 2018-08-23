package br.com.alanlb.receitas.model;

public class UsuarioLogado {
    private static String id;
    private static boolean logado;


    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        UsuarioLogado.id = id;
    }

    public static boolean isLogado() {
        return logado;
    }

    public static void setLogado(boolean logado) {
        UsuarioLogado.logado = logado;
    }
}
