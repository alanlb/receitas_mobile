package br.com.alanlb.receitas.model;

import java.util.ArrayList;

import br.com.alanlb.receitas.dao.HistoricoDao;
import br.com.alanlb.receitas.exception.SqliteException;

public class HistoricoProxy {
    private Historico historicoClass;
    private int id;

    public Historico getHistorico(/*id*/) throws SqliteException {
        if(historicoClass == null){
            historicoClass = HistoricoDao.buscarPorID(/*id*/);
            return historicoClass;
        }
        return historicoClass;
    }

    public Historico getObserver(){
        if(historicoClass == null){
            historicoClass = new Historico();
        }
        return historicoClass;
    }
}
