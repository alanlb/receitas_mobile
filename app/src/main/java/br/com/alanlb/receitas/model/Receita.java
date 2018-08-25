package br.com.alanlb.receitas.model;

import android.graphics.Bitmap;

import br.com.alanlb.receitas.util.Util;

public class Receita {
    private int id;
    private String idFireBase;
    private String nome;
    private String ingredientes;
    private String modoDePreparo;
    private String id_usuario;
    private String pathImg;
    private String url;

    public Receita(String nome, String ingredientes, String modoDePreparo){
        this.setNome(nome);
        this.setIngredientes(ingredientes);
        this.setModoDePreparo(modoDePreparo);
    }
    public Receita(String nome, String ingredientes, String modoDePreparo, String id_usuario, String pathImg, String url){
        this.id = 0;
        this.setNome(nome);
        this.setIngredientes(ingredientes);
        this.setModoDePreparo(modoDePreparo);
        this.setId_usuario(id_usuario);
        this.setPathImg(pathImg);
        this.setUrl(url);
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
        sql.append("'" + this.getIdFireBase() + "',");
        sql.append("'" + this.getNome() + "',");
        sql.append("'" + this.getIngredientes() + "',");
        sql.append("'" + this.getModoDePreparo() + "',");
        sql.append("'" + this.getId_usuario() + "',");
        sql.append("'" + this.getPathImg() + "',");
        sql.append("'" + this.getUrl() + "'");
        return sql.toString();
    }


    public String getIdFireBase() {
        return idFireBase;
    }

    public void setIdFireBase(String idFireBase) {
        this.idFireBase = idFireBase;
    }

    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg) {
        this.pathImg = pathImg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
