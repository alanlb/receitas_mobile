package br.com.alanlb.receitas.dao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.alanlb.receitas.dao.firebase.FireBaseBD;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Receita;

public class ReceitaDAOFireBase{
    private static DatabaseReference reference;


    public static void salvarReceita(Context context,Receita receita) throws SqliteException {
        reference = FireBaseBD.getReference(context);
        reference.child("Receita").push().setValue(receita);
    }

    public void alterarReceita(Context context, Receita receita){

    }

    public ArrayList<Receita> buscarReceitaPorNome(Context context, String nome) throws SqliteException {
        return null;
    }

    public List<Receita> buscarToasReceitas(Context context) throws SqliteException {
        return null;
    }

    public ArrayList<Receita> buscarReceitaPorUsuario(Context context, int id) throws SqliteException {
        return null;
    }

    public static void deletarReceita(final Context context, Receita receita) throws SqliteException {
        reference = FireBaseBD.getReference(context);
        reference.child("Receita").child(receita.getIdFireBase()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Deletado com sucesso", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Falha ao deletar", Toast.LENGTH_SHORT).show();
            }
        });

        Facade.deletarReceita(context, receita.getId());
    }


    public static void atualizarReceitaFirebase(final Context context, Receita receita) throws SqliteException {
        reference = FireBaseBD.getReference(context);
        Map<String, Object> receitaObj = new HashMap<>();
//        receitaObj.put(receita.getIdFireBase(), new Receita(receita.getNome(),
//                                                            receita.getIngredientes(),
//                                                            receita.getModoDePreparo(),
//                                                            receita.getId_usuario(),
//                                                            receita.getPathImg(),
//                                                            receita.getUrl()));
        receitaObj.put("id",receita.getId());
        receitaObj.put("id_usuario",receita.getId_usuario());
        receitaObj.put("nome",receita.getNome());
        receitaObj.put("ingredientes",receita.getIngredientes());
        receitaObj.put("modoDePreparo",receita.getModoDePreparo());
        receitaObj.put("pathImg",receita.getPathImg());
        //reference.child("receita").child(receita.getIdFireBase());
        DatabaseReference referenceReceita = reference.child("Receita").child(receita.getIdFireBase());
        referenceReceita.updateChildren(receitaObj);


    }
    public void deletarTabela(Context context) throws SqliteException {

    }
}
