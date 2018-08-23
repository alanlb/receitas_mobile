package br.com.alanlb.receitas.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Item {
    private String nome;
    private Bitmap imagem;
    public Item(String nome, Bitmap imagem){
        this.setImagem(imagem);
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
}
