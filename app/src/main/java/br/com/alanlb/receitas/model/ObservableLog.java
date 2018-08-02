package br.com.alanlb.receitas.model;

public interface ObservableLog {
    public void registerObserver(ObserverLog observer);
    public void removeObserver(ObserverLog observer);
    public void notifyObservers(String s);
}
