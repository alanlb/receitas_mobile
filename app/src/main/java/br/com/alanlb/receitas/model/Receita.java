package br.com.alanlb.receitas.model;

import br.com.alanlb.receitas.util.Util;

public class Receita {
    private int id;
    private String idFireBase;
    private String nome;
    private String ingredientes;
    private String modoDePreparo;
    private String id_usuario;

    public Receita(String nome, String ingredientes, String modoDePreparo){
        this.setNome(nome);
        this.setIngredientes(ingredientes);
        this.setModoDePreparo(modoDePreparo);
    }

    public Receita(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getModoDePreparo() {
        return modoDePreparo;
    }

    public void setModoDePreparo(String modoDePreparo) {
        this.modoDePreparo = modoDePreparo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Object getToSqlInsert() {
        StringBuffer sql = new StringBuffer();
        //sql.append(this.getId() + ",");
        sql.append("'" + this.getNome() + "',");
        sql.append("'" + this.getIngredientes() + "',");
        sql.append("'" + this.getModoDePreparo() + "',");
        sql.append("'" + this.getId_usuario() + "'");
        return sql.toString();
    }


    public String getIdFireBase() {
        return idFireBase;
    }

    public void setIdFireBase(String idFireBase) {
        this.idFireBase = idFireBase;
    }
}
