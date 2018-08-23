package br.com.alanlb.receitas.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {
    private Context context;
    private SharedPreferences preferences;
    private String nomeArquivo = "receitas.preferencias";
    private int mode = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificarUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";

    public Preferencias(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(nomeArquivo, mode);
        editor = preferences.edit();
    }

    public void salvarUsuarioPreferencias(String identificadorUsuaria, String nomeUsuario){
        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuaria);
        editor.putString(CHAVE_NOME, nomeUsuario);
        editor.commit();
    }

    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome(){
        return preferences.getString(CHAVE_NOME, null);
    }
}
