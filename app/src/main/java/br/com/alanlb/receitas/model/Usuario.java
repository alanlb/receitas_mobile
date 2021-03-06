package br.com.alanlb.receitas.model;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.alanlb.receitas.dao.ConfiguracaoFireBase;
import br.com.alanlb.receitas.dao.firebase.FireBaseBD;
import br.com.alanlb.receitas.util.Util;

public class Usuario {
    private Integer id;
    private String idFireBase;
    private boolean sincronizado;

    private String nomeCompleto;
    private String login;
    private String senha;

    private Date dataCadastro;
    private Date ultimoAcesso;


    public Usuario() {
        this.dataCadastro = new Date();
        this.ultimoAcesso= new Date();
        this.sincronizado = true;
    }

    public void salvar(Context context){
        DatabaseReference databaseReference = FireBaseBD.getReference(context);
        databaseReference.child("usuario").child(String.valueOf(getIdFireBase())).setValue(this);
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapUsuario = new HashMap<>();

        hashMapUsuario.put("id", getId());
        hashMapUsuario.put("idFireBase", getIdFireBase());
        hashMapUsuario.put("sincronizado", isSincronizado());

        hashMapUsuario.put("nome", getNomeCompleto());
        hashMapUsuario.put("login", getLogin());
        hashMapUsuario.put("senha", getSenha());

        hashMapUsuario.put("dataCadastro", getDataCadastro());
        hashMapUsuario.put("ultimoAcesso", getUltimoAcesso());

        return hashMapUsuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(boolean sincronizado) {
        this.sincronizado = sincronizado;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Date getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(Date ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }
    public Object getToSqlInsert() {
        StringBuffer sql = new StringBuffer();
        sql.append(this.id + ",");
        sql.append("'" + this.idFireBase + "',");
        sql.append("'" + this.nomeCompleto + "',");
        sql.append("'" + this.login + "',");
        sql.append("'" + this.senha + "',");
        sql.append("'" + this.sincronizado + "',");
        sql.append("'" + Util.getStringToFormaterDate(this.dataCadastro, Util.dataTimeFormater_SQLITE) + "',");
        sql.append("'" + Util.getStringToFormaterDate(this.ultimoAcesso, Util.dataTimeFormater_SQLITE) + "'");
        return sql.toString();
    }

    public String getIdFireBase() {
        return idFireBase;
    }

    public void setIdFireBase(String idFireBase) {
        this.idFireBase = idFireBase;
    }
}
