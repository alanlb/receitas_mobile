package br.com.alanlb.receitas.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import br.com.alanlb.receitas.R;
import br.com.alanlb.receitas.control.CreateListView;
import br.com.alanlb.receitas.util.ListAdapterItem;

public class MinhasReceitasFragment extends Fragment {
    private ListView lv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.minhas_receitas_frag, null);
        lv = (ListView)view.findViewById(R.id.lv_receitas);
        Bundle bundle = getArguments();
        int idUsuario = bundle.getInt("ID");

        CreateListView create = new CreateListView(getContext(), lv, idUsuario);
        return view;
    }

    public void adicionaItens(ListAdapterItem adapter){

        //lv.setAdapter(adapter);
        //lv.setOnItemClickListener(new ItemListEvent());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
