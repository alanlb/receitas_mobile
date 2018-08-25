package br.com.alanlb.receitas.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.alanlb.receitas.R;
import br.com.alanlb.receitas.model.Item;

public class ListAdapterItem extends ArrayAdapter<Item> {
    private Context context;
    private ArrayList<Item> lista;

    public ListAdapterItem(Context context, ArrayList<Item> lista) {
        super(context, 0, lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item itemAtual = this.lista.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.item_layout, null);



        TextView nome = (TextView) convertView.findViewById(R.id.nome_item);
        nome.setText(itemAtual.getNome());

        ImageView imagem = (ImageView) convertView.findViewById(R.id.imageitem);
        if(itemAtual.getUrl() != null){
            Picasso.get().load(itemAtual.getUrl())
                    .resize(50, 50)
                    .centerCrop()
                    .into(imagem);
        }else {
            imagem.setImageBitmap(itemAtual.getImagem());
        }
        return convertView;
    }
}
