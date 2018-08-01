package br.com.alanlb.receitas.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.alanlb.receitas.R;

public class PrincipalFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.principal_frag, null);

        return view;
    }

//    public void alteraTitulo(String titulo){
//        TextView textTitulo = (TextView) getView().findViewById(R.id.testviewfragtitulo);
//        textTitulo.setText(titulo);
//    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
