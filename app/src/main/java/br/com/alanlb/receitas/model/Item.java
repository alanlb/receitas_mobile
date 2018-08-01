package br.com.alanlb.receitas.model;

public class Item {
    private String nome;
    private int id;
    public Item(String nome, int id){
        this.setId(id);
        this.setNome(nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
