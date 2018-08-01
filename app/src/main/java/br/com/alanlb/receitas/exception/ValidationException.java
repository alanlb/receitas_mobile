package br.com.alanlb.receitas.exception;


import android.widget.Toast;

public class ValidationException extends Exception {

    public ValidationException(String msg){
        super(msg);
    }
}
