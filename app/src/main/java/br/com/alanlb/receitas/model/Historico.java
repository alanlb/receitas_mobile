package br.com.alanlb.receitas.model;

import java.util.ArrayList;

public class Historico implements ObserverLog{
    private ArrayList<String> historicos;

    public Historico(){
        historicos = new ArrayList<String>();
        historicos.add("Históricos: ");
    }
    @Override
    public void update(String historico) {
        historicos.add(historico);
        //System.out.println("------------------------------ "+historicos.size()+" ---------------------------------");
    }

    public String getHistoricos(){
        return historicos.toString();
    }
}
