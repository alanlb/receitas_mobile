package br.com.alanlb.receitas.dao.firebase;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseBD {
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;

    public static synchronized DatabaseReference getReference(Context context){
        if (databaseReference == null){
            FirebaseApp.initializeApp(context);
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
            databaseReference = firebaseDatabase.getReference();
        }
        return databaseReference;
    }
}
