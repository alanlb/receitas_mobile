package br.com.alanlb.receitas.model;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public abstract class Observable extends AppCompatActivity implements ObservableLog{

    private ArrayList<ObserverLog> observers;
    private String historico;

    public Observable(){
        observers = new ArrayList<ObserverLog>();
    }

    @Override
    public void registerObserver(ObserverLog observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ObserverLog observer) {
        observers.remove(observer);
    }

    public void setHistorico(String s){
        notifyObservers(s);
    }



    @Override
    public void notifyObservers(String s) {
        for(ObserverLog observer: observers)
            observer.update(s);
    }
}
