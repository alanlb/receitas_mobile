package br.com.alanlb.receitas.model;

import java.util.ArrayList;

public class HistoricoProxy {
    private Historico historicoClass;

    public String getHistorico(){
        if(historicoClass == null){
            historicoClass = new Historico();
        }
        return historicoClass.getHistoricos();
    }

    public Historico getObserver(){
        if(historicoClass == null){
            historicoClass = new Historico();
        }
        return historicoClass;
    }
}
