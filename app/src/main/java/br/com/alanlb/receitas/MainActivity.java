package br.com.alanlb.receitas;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.alanlb.receitas.control.CreateListView;
import br.com.alanlb.receitas.control.DropListener;
import br.com.alanlb.receitas.dao.AbstractFactoryDAO;
import br.com.alanlb.receitas.dao.Facade;
import br.com.alanlb.receitas.dao.ReceitaDAO;
import br.com.alanlb.receitas.dao.ReceitaDAOSqlite;
import br.com.alanlb.receitas.dao.SingletonFactory;
import br.com.alanlb.receitas.dao.UsuarioDAOSqlite;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.model.Historico;
import br.com.alanlb.receitas.model.HistoricoProxy;
import br.com.alanlb.receitas.model.Item;
import br.com.alanlb.receitas.model.Observable;
import br.com.alanlb.receitas.model.Receita;
import br.com.alanlb.receitas.model.SGBD;
import br.com.alanlb.receitas.model.Usuario;
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
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        observerLog  = new HistoricoProxy();
        super.onCreate(savedInstanceState);
        observer  =  observerLog.getObserver();
        registerObserver(observer);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emailUsuario = bundle.getString("EMAIL");
        nomeUsuario = bundle.getString("NOME");
        idUsuario = bundle.getInt("ID");
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
            return true;
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
            bundle.putInt("ID",idUsuario);
            frag.setArguments(bundle);
            ft.commit();
            System.out.println(""+observer.getHistoricos());
        } else if (id == R.id.adicionar_receitas) {
            AdicionarReceitasFragment frag = new AdicionarReceitasFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.layoutparafragmentos, frag,"adicionar_receitas");
            ft.commit();
            System.out.println(""+observer.getHistoricos());
        } else if (id == R.id.pesquisa_receita) {
            PesquisaReceitasFragment frag = new PesquisaReceitasFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.layoutparafragmentos, frag,"pesquisa_receitas");
            ft.commit();
            System.out.println(""+observer.getHistoricos());
        } else if (id == R.id.deletar_receita) {
            DeletarReceitaFragment frag = new DeletarReceitaFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.layoutparafragmentos, frag,"pesquisa_receitas");
            ft.commit();
            System.out.println(""+observer.getHistoricos());
        } //else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cadastraReceita(View view){
        EditText nome = findViewById(R.id.nome_cadastro_receita);
        EditText ingredientes = findViewById(R.id.igredientes_textarea);
        EditText modoDePreparo = findViewById(R.id.modo_de_preparo_textarea);

        Receita receita = new Receita();

        receita.setNome(nome.getText().toString());
        receita.setIngredientes(ingredientes.getText().toString());
        receita.setModoDePreparo(modoDePreparo.getText().toString());

        Usuario u = new Usuario();

        try {
            u = Facade.buscarUsuarioPorID(this, this.idUsuario);
            receita.setId_usuario(u.getId());

            Facade.cadastrarReceita(this,receita);
            Toast.makeText(this, "Receita Salva com sucesso!",Toast.LENGTH_SHORT).show();

            PrincipalFragment frag = new PrincipalFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.layoutparafragmentos, frag,"principal_frag");
            ft.commit();
            this.setHistorico("Receita Criada com sucesso");
        } catch (SqliteException e) {
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
            this.setHistorico("Erro ao criar receita");
        }
    }

    public void pesquisaReceita(View view){
        EditText nome = findViewById(R.id.pesquisaEditText);
        try {
            ArrayList<Receita> receitas = Facade.buscarReceitaPorNome(this,nome.getText().toString());
            if (receitas.size() == 0){
                Toast.makeText(this, "Nenhum resultado para esta pesquisa",Toast.LENGTH_SHORT).show();
            }else{
                ArrayList<Item> itens = new ArrayList<Item>();
                for (Receita receita: receitas){
                    itens.add(new Item(receita.getNome(),receita.getId()));
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
                for (Receita receita: receitas){
                    itens.add(new Item(receita.getNome(),receita.getId()));
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
}
