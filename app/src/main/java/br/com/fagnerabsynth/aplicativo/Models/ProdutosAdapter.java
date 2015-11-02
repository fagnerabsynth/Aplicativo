package br.com.fagnerabsynth.aplicativo.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.fagnerabsynth.aplicativo.R;


public class ProdutosAdapter extends BaseAdapter {
    private Context context;
    private List<ProdutosMOD> listadeDados;

    public ProdutosAdapter(Context context, List<ProdutosMOD> statelist) {
        this.context = context;
        this.listadeDados = statelist;
    }

    @Override
    public int getCount() {
        return listadeDados.size();
    }

    @Override
    public Object getItem(int position) {
        return listadeDados.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ProdutosMOD dados = listadeDados.get(position);

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.listaritem, null);

        TextView nome = (TextView) view.findViewById(R.id.nome);
        nome.setText(dados.nome);

        TextView valor = (TextView) view.findViewById(R.id.valor);
        valor.setText("R$ " + dados.valor);
/*

        TextView descricao = (TextView) view.findViewById(R.id.descricao);
        descricao.setText(dados.descricao);


        TextView categoria = (TextView) view.findViewById(R.id.categoria);
        categoria.setText(dados.categoria);

        TextView id = (TextView) view.findViewById(R.id.id);
        id.setText("" + dados.id);
  */
        return view;
    }
}