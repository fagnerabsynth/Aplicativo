package br.com.fagnerabsynth.aplicativo.Views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.fagnerabsynth.aplicativo.Data.Conexao;
import br.com.fagnerabsynth.aplicativo.Models.App;
import br.com.fagnerabsynth.aplicativo.Models.ProdutosMOD;
import br.com.fagnerabsynth.aplicativo.R;

public class MostrarProduto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrarproduto);

        String nomeRecebido = getIntent().getStringExtra("nome").toString();
        Conexao con = new Conexao(this);

        ProdutosMOD produtos = con.pesquisaProduto(nomeRecebido);

        TextView nome = (TextView) findViewById(R.id.produto);
        nome.setText(produtos.nome);

        TextView novaCategoria = (TextView) findViewById(R.id.spinner);
        novaCategoria.setText(produtos.categoria);

        TextView preco = (TextView) findViewById(R.id.preco);
        preco.setText("R$ " + produtos.valor);

        TextView ativo = (TextView) findViewById(R.id.ativo);
        ativo.setText("" + produtos.ativo);

        TextView descricao = (TextView) findViewById(R.id.descricao);
        descricao.setText(produtos.descricao);


        String SUBTITULO;
        SUBTITULO = "Produto: " + produtos.nome;
        String TITULO = new App().getNome();
        getSupportActionBar().setTitle(TITULO);
        getSupportActionBar().setSubtitle(SUBTITULO);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                int id = item.getItemId();
                if (id == R.id.action_settings) {
                    return true;
                }
                return super.onOptionsItemSelected(item);
        }
    }
}
