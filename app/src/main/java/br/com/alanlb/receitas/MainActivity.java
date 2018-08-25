package br.com.alanlb.receitas;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import br.com.alanlb.receitas.control.CreateListView;
import br.com.alanlb.receitas.control.DropListener;
import br.com.alanlb.receitas.control.LoginActivity;
import br.com.alanlb.receitas.dao.Facade;
import br.com.alanlb.receitas.dao.ReceitaDAOFireBase;
import br.com.alanlb.receitas.dao.firebase.FireBaseBD;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Historico;
import br.com.alanlb.receitas.model.HistoricoProxy;
import br.com.alanlb.receitas.model.Item;
import br.com.alanlb.receitas.model.Observable;
import br.com.alanlb.receitas.model.Receita;
import br.com.alanlb.receitas.model.Usuario;
import br.com.alanlb.receitas.model.UsuarioLogado;
import br.com.alanlb.receitas.util.ListAdapterItem;
import br.com.alanlb.receitas.view.AdicionarReceitasFragment;
import br.com.alanlb.receitas.view.DeletarReceitaFragment;
import br.com.alanlb.receitas.view.MinhasReceitasFragment;
import br.com.alanlb.receitas.view.PesquisaReceitasFragment;
import br.com.alanlb.receitas.view.PrincipalFragment;

public class MainActivity extends Observable
        implements NavigationView.OnNavigationItemSelectedListener {

    private HistoricoProxy observerLog;
    private  String nomeUsuario;
    private String emailUsuario;
    private Historico observer;
    private String idUsuario;
    public static final int IMAGEM_INTERNA = 12;
    private Uri filePath;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        observerLog  = new HistoricoProxy();
        super.onCreate(savedInstanceState);
        observer  =  observerLog.getObserver();
        registerObserver(observer);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUrl("");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emailUsuario = bundle.getString("EMAIL");
        nomeUsuario = bundle.getString("NOME");
        idUsuario = bundle.getString("ID");
        Toast.makeText(this, emailUsuario, Toast.LENGTH_SHORT).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        TextView nome = header.findViewById(R.id.nome_usuario_logado);
        nome.setText(nomeUsuario);
        TextView email = header.findViewById(R.id.email_logado);
        email.setText(emailUsuario);

        eventoDataBase();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            UsuarioLogado.setLogado(false);
            UsuarioLogado.setId("");
            Intent intent = new Intent(this, LoginActivity.class);
            try {
                Facade.deletarTabelaPorId(this,1);
            } catch (SqliteException e) {
                e.printStackTrace();
            }
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.minhas_receitas) {
            MinhasReceitasFragment frag = new MinhasReceitasFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.layoutparafragmentos, frag,"minhas_receitas");
            Bundle bundle = new Bundle();
            bundle.putString("ID",idUsuario);
            frag.setArguments(bundle);
            ft.commit();
        } else if (id == R.id.adicionar_receitas) {
            AdicionarReceitasFragment frag = new AdicionarReceitasFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.layoutparafragmentos, frag,"adicionar_receitas");
            ft.commit();
        } else if (id == R.id.pesquisa_receita) {
            PesquisaReceitasFragment frag = new PesquisaReceitasFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.layoutparafragmentos, frag,"pesquisa_receitas");
            ft.commit();
        } else if (id == R.id.deletar_receita) {
            DeletarReceitaFragment frag = new DeletarReceitaFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.layoutparafragmentos, frag,"pesquisa_receitas");
            ft.commit();
        } //else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void editarReceita(View view){


    }

    public void cadastraReceitaFireBase(View view){
        EditText nome = findViewById(R.id.nome_cadastro_receita);
        EditText ingredientes = findViewById(R.id.igredientes_textarea);
        EditText modoDePreparo = findViewById(R.id.modo_de_preparo_textarea);

        //Bitmap imagem = BitmapFactory.decodeFile(pathImg.getText().toString());
        String path = "";

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();




        Receita receita = new Receita();


        receita.setNome(nome.getText().toString());
        receita.setIngredientes(ingredientes.getText().toString());
        receita.setModoDePreparo(modoDePreparo.getText().toString());

        try {
            Usuario usuario = Facade.buscarUsuarioPorID(this,1);
            if(filePath != null) {
                StorageReference riversRef = storageReference.child("imagens/receitas/" + filePath.getLastPathSegment());
                path = "imagens/receitas/" + filePath.getLastPathSegment();
                Toast.makeText(getBaseContext(), "" + filePath, Toast.LENGTH_SHORT).show();

                UploadTask uploadTask = riversRef.putFile(filePath);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getBaseContext(), "Erro ao salvar imagem", Toast.LENGTH_SHORT).show();
                        exception.printStackTrace();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });
                filePath = null;
            }
            receita.setPathImg(path);
            receita.setId_usuario(usuario.getIdFireBase());
            ReceitaDAOFireBase.salvarReceita(this,receita);

            Toast.makeText(this,"Receita cadastrada com sucesso!", Toast.LENGTH_SHORT).show();

            PrincipalFragment frag = new PrincipalFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.layoutparafragmentos, frag,"principal_frag");
            ft.commit();
        } catch (SqliteException e) {
            e.printStackTrace();
        }
    }

//    public void cadastraReceita(View view){
//        EditText nome = findViewById(R.id.nome_cadastro_receita);
//        EditText ingredientes = findViewById(R.id.igredientes_textarea);
//        EditText modoDePreparo = findViewById(R.id.modo_de_preparo_textarea);
//
//        Receita receita = new Receita();
//
//        receita.setNome(nome.getText().toString());
//        receita.setIngredientes(ingredientes.getText().toString());
//        receita.setModoDePreparo(modoDePreparo.getText().toString());
//
//        Usuario u = new Usuario();
//
//        try {
//            u = Facade.buscarUsuarioPorID(this, 0);
//            receita.setId_usuario(u.getIdFireBase());
//
//            Facade.cadastrarReceita(this,receita);
//            Toast.makeText(this, "Receita Salva com sucesso!",Toast.LENGTH_SHORT).show();
//
//            PrincipalFragment frag = new PrincipalFragment();
//            FragmentManager fm = getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.replace(R.id.layoutparafragmentos, frag,"principal_frag");
//            ft.commit();
//            this.setHistorico("Receita Criada com sucesso");
//        } catch (SqliteException e) {
//            Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
//            this.setHistorico("Erro ao criar receita");
//        }
//    }

    public void pesquisaReceita(View view){
        EditText nome = findViewById(R.id.pesquisaEditText);
        try {
            ArrayList<Receita> receitas = Facade.buscarReceitaPorNome(this,nome.getText().toString());
            if (receitas.size() == 0){
                Toast.makeText(this, "Nenhum resultado para esta pesquisa",Toast.LENGTH_SHORT).show();
            }else{
                ArrayList<Item> itens = new ArrayList<Item>();
                Bitmap imagem = BitmapFactory.decodeResource(this.getResources(),R.drawable.talheres);
                for (Receita receita: receitas){
                    itens.add(new Item(receita.getNome(),imagem));
                }
                ListAdapterItem adapter = new ListAdapterItem(this,itens);

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.minhas_receitas_dialog_frag);
                dialog.setTitle("Resultados");
                ListView lv = (ListView) dialog.findViewById(R.id.lv_receitas_dialog);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new CreateListView(this, receitas, dialog));
                dialog.show();
                this.setHistorico("Pesquisa de receita realizada");
            }
        } catch (SqliteException e) {
            Toast.makeText(this, "Erro ao pesquisar receita",Toast.LENGTH_SHORT).show();
            this.setHistorico("Erro ao pesquisar receita");
        }
        System.out.println(observer.getHistoricos());
    }

    public void apagarReceita(View view){
        EditText nome = findViewById(R.id.deletarEditText);
        try {
            ArrayList<Receita> receitas = Facade.buscarReceitaPorNome(this,nome.getText().toString());
            if (receitas.size() == 0){
                Toast.makeText(this, "Nenhum resultado para esta pesquisa",Toast.LENGTH_SHORT).show();
            }else{
                ArrayList<Item> itens = new ArrayList<Item>();
                Bitmap imagem = BitmapFactory.decodeResource(this.getResources(),R.drawable.talheres);
                for (Receita receita: receitas){
                    itens.add(new Item(receita.getNome(),imagem));
                }
                ListAdapterItem adapter = new ListAdapterItem(this,itens);

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.minhas_receitas_dialog_frag);
                dialog.setTitle("Resultados");
                ListView lv = (ListView) dialog.findViewById(R.id.lv_receitas_dialog);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new DropListener(this, receitas, dialog));
                dialog.show();
            }
        } catch (SqliteException e) {
            Toast.makeText(this, "Erro ao pesquisar receita",Toast.LENGTH_SHORT).show();
            this.setHistorico("Erro ao apagar receita");
        }

    }



    public void eventoDataBase(){
        DatabaseReference reference = FireBaseBD.getReference(this);
        reference.child("Receita").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Facade.deletarTabelaReceita(getBaseContext());
                    Facade.criarTabelaReceita(getBaseContext());
                    for (DataSnapshot obj: dataSnapshot.getChildren()){
                        final Receita r = obj.getValue(Receita.class);
                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                        StorageReference storageRef = firebaseStorage.getReference();
                        r.setIdFireBase(obj.getKey());

                        System.out.println("RETORNOUUUUU >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+Facade.isReceitaSincronizada(getBaseContext(), obj.getKey()));
                        if(!Facade.isReceitaSincronizada(getBaseContext(), obj.getKey())) {
                            if (!r.getPathImg().equals("")) {

                                storageRef.child(r.getPathImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        setUrl(uri.toString());
                                        r.setUrl(getUrl());
                                        try {
                                            Facade.cadastrarReceita(getBaseContext(), r);
                                        } catch (SqliteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

//                            System.out.println("-----------------------------------" + getUrl());
//                            r.setUrl(getUrl());
//                            Facade.cadastrarReceita(getBaseContext(), r);
                                //setUrl("");
                            } else {
                                r.setUrl("");
                                Facade.cadastrarReceita(getBaseContext(), r);
                            }
                        }

                    }
                } catch (SqliteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    //###################### IMAGEM ################################

    public void pegaImagemReceita(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGEM_INTERNA);
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent){
        if(requestCode == IMAGEM_INTERNA){
            if(responseCode == RESULT_OK){

                Uri imagemSelecionada = intent.getData();
                filePath = imagemSelecionada;
//                String[] colunas = {MediaStore.Images.Media.DATA};

//                Cursor cursor = getContentResolver().query(imagemSelecionada, colunas, null, null, null);
//                cursor.moveToFirst();

//                int indexColuna = cursor.getColumnIndex(colunas[0]);
//                String pathImg = cursor.getString(indexColuna);

//                cursor.close();
//
//                TextView pathImagemTextView = (TextView) findViewById(R.id.pathimagem);
//                pathImagemTextView.setText(pathImg);

                ImageView imagemView = (ImageView)findViewById(R.id.imageviewselecionada);
                Bitmap imagem = null;
                try {
                    imagem = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                    imagemView.setImageBitmap(imagem);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
