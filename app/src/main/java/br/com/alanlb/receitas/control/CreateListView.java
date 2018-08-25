package br.com.alanlb.receitas.control;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.alanlb.receitas.MainActivity;
import br.com.alanlb.receitas.R;
import br.com.alanlb.receitas.dao.Facade;
import br.com.alanlb.receitas.dao.ReceitaDAOSqlite;
import br.com.alanlb.receitas.dao.SingletonFactory;
import br.com.alanlb.receitas.dao.firebase.FireBaseBD;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Item;
import br.com.alanlb.receitas.model.Receita;
import br.com.alanlb.receitas.model.Usuario;
import br.com.alanlb.receitas.util.ListAdapterItem;
import br.com.alanlb.receitas.view.ReceitaFrag;

public class CreateListView implements AdapterView.OnItemClickListener {
    private Context context;
    private ListView listView;
    private String id;
    private ArrayList<Receita> receitas;
    private Dialog dialog;
    private static final String TAG = "Panoramio";
    private boolean proprioUsuarioDonoReceita;
    private Usuario usuario;

    private static final int IO_BUFFER_SIZE = 4 * 1024;

    public CreateListView(Context context, ListView listView, String id){
        this.context = context;
        this.listView = listView;
        this.id = id;
        this.proprioUsuarioDonoReceita = false;

        criar(context, listView, id);
    }

    public CreateListView(Context context, String nomePesquisa, ListView listView, Dialog dialog){
        this.context = context;
        this.listView = listView;
        this.id = id;
        this.proprioUsuarioDonoReceita = false;
        this.dialog = dialog;

        criar(context, nomePesquisa, listView);
    }

    public CreateListView(Context context, ArrayList<Receita> lista, Dialog dialog){
         this.receitas = lista;
         this.context = context;
         this.dialog = dialog;
    }

    public void criar(Context context, ListView listView, String id){
        try {
            ArrayList<Receita> receitas = Facade.buscarReceitaPorUsuario(context, id);
            //ArrayList<Receita> receitas = SingletonFactory.getFactory().getReceitaDAO().buscarReceitaPorUsuario(context, id);
            this.receitas = receitas;
            ArrayList<Item> itens = new ArrayList<Item>();
            this.usuario = Facade.buscarUsuarioPorID(context,1);
            for(Receita receita: receitas){
                if(receita.getUrl().equals("")){
                    Bitmap imagem = BitmapFactory.decodeResource(context.getResources(),R.drawable.talheres);
                    itens.add(new Item(receita.getNome(),imagem));
                }else{
//                    URL url = new URL(receita.getUrl());
                    //Bitmap imagem = loadBitmap(receita.getUrl());
                    itens.add(new Item(receita.getNome(),receita.getUrl()));
                }
//                Bitmap imagem = BitmapFactory.decodeResource(context.getResources(),R.drawable.talheres);
//                itens.add(new Item(receita.getNome(),imagem));
                if(this.usuario.getIdFireBase().equals(receita.getId_usuario())){
                    proprioUsuarioDonoReceita = true;
                }else{
                    proprioUsuarioDonoReceita = false;
                }
            }


            ListAdapterItem adapter = new ListAdapterItem(context , itens);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(null);
            listView.setOnItemClickListener(this);
        } catch (SqliteException e) {
            new SqliteException("Não foi possível achar a receita");
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void criar(Context context, String nomePesquisa, ListView listView){
        try {
            ArrayList<Receita> receitas = Facade.buscarReceitaPorNome(context,nomePesquisa);
            //ArrayList<Receita> receitas = SingletonFactory.getFactory().getReceitaDAO().buscarReceitaPorUsuario(context, id);
            this.receitas = receitas;
            ArrayList<Item> itens = new ArrayList<Item>();
            this.usuario = Facade.buscarUsuarioPorID(context,1);
            for(Receita receita: receitas){
                if(receita.getUrl().equals("")){
                    Bitmap imagem = BitmapFactory.decodeResource(context.getResources(),R.drawable.talheres);
                    itens.add(new Item(receita.getNome(),imagem));
                }else{
//                    URL url = new URL(receita.getUrl());
                    //Bitmap imagem = loadBitmap(receita.getUrl());
                    itens.add(new Item(receita.getNome(),receita.getUrl()));
                }
//                Bitmap imagem = BitmapFactory.decodeResource(context.getResources(),R.drawable.talheres);
//                itens.add(new Item(receita.getNome(),imagem));

                //System.out.println("TESTE >> "+usuario.getIdFireBase()+" ? "+receita.getId_usuario()+" = "+proprioUsuarioDonoReceita);
            }


            ListAdapterItem adapter = new ListAdapterItem(context , itens);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(null);
            listView.setOnItemClickListener(this);
        } catch (SqliteException e) {
            new SqliteException("Não foi possível achar a receita");
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> lv, View view, int position, long id) {
        System.out.println("Receita nome: " + this.receitas.get(position).getNome() +
                            "Id: " + this.receitas.get(position).getId() +
                            "Receita: " + this.receitas.get(position).getIngredientes());

//      ############ É dono da receita? ###########################################

        if(usuario.getIdFireBase().equals(receitas.get(position).getId_usuario())){
            proprioUsuarioDonoReceita = true;
        }else{
            proprioUsuarioDonoReceita = false;
        }

//       ###########################################################################
        Bundle bundle = new Bundle();
        bundle.putInt("ID",receitas.get(position).getId());
        bundle.putString("IDFIREBASE",receitas.get(position).getIdFireBase());
        bundle.putString("NOME",receitas.get(position).getNome());
        bundle.putString("INGREDIENTES", receitas.get(position).getIngredientes());
        bundle.putString("MODODEPREPARO", receitas.get(position).getModoDePreparo());
        bundle.putString("IDUSUARIO", receitas.get(position).getId_usuario());
        bundle.putString("PATHIMG", receitas.get(position).getPathImg());
        bundle.putString("URL", receitas.get(position).getUrl());
        bundle.putBoolean("DONO",proprioUsuarioDonoReceita);

        ReceitaFrag frag = new ReceitaFrag();
        FragmentManager fm = ((MainActivity)this.context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        frag.setArguments(bundle);
        ft.replace(R.id.layoutparafragmentos, frag,"receita_frag");
        ft.commit();

        if(dialog != null){
            dialog.cancel();
        }
    }

    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();

            final byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (IOException e) {
            Log.e(TAG, "Could not load Bitmap from: " + url);
        } finally {
            closeStream(in);
            closeStream(out);
        }

        return bitmap;
    }
    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                android.util.Log.e(TAG, "Could not close stream", e);
            }
        }
    }

}
