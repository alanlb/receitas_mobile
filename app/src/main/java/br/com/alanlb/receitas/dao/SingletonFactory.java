package br.com.alanlb.receitas.dao;

import br.com.alanlb.receitas.model.SGBD;

public class SingletonFactory {
   private static AbstractFactoryDAO factory;

    public synchronized static AbstractFactoryDAO getFactory(){
        if(factory == null){
            factory = AbstractFactoryDAO.getInstance(SGBD.SQLITE);
            return factory;
        }
        return factory;
    }
}
