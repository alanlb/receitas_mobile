package br.com.alanlb.receitas.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.net.URL;

public class Item {
    private String nome;
    private Bitmap imagem;
    private String url;
    public Item(String nome, Bitmap imagem){
        this.setImagem(imagem);
        this.setNome(nome);
    }

    public Item(String nome, String url){
        this.setUrl(url);
        this.setNome(nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public Bitmap getImagem() {
        return imagem;
    }

    public void setImagem(Bitmap imagem) {
        this.imagem = imagem;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
