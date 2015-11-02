package br.com.fagnerabsynth.aplicativo.Views;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.fagnerabsynth.aplicativo.Data.Conexao;
import br.com.fagnerabsynth.aplicativo.Models.App;
import br.com.fagnerabsynth.aplicativo.Models.ProdutosMOD;
import br.com.fagnerabsynth.aplicativo.R;

public class MostrarProduto extends AppCompatActivity {
    private String no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrarproduto);

        String nomeRecebido = getIntent().getStringExtra("nome").toString();
        Conexao con = new Conexao(this);

        ProdutosMOD produtos = con.pesquisaProduto(nomeRecebido, 0);

        TextView nome = (TextView) findViewById(R.id.produto);
        nome.setText(produtos.nome);
        no = produtos.nome;
        TextView novaCategoria = (TextView) findViewById(R.id.spinner);
        novaCategoria.setText(produtos.categoria);

        TextView preco = (TextView) findViewById(R.id.preco);
        preco.setText("R$ " + produtos.valor);

        String ati;
        if (produtos.ativo == 1) {
            ati = "Produto ativado!";
        } else {
            ati = "Produto Desativado!";
        }

        TextView ativo = (TextView) findViewById(R.id.ativo);
        ativo.setText(ati);

        TextView descricao = (TextView) findViewById(R.id.descricao);
        descricao.setText(produtos.descricao);


        String SUBTITULO;
        SUBTITULO = "Produto: " + produtos.nome;
        String TITULO = new App().getNome();
        getSupportActionBar().setTitle(TITULO);
        getSupportActionBar().setSubtitle(SUBTITULO);
        getSupportActionBar().setIcon(R.mipmap.logo);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void apagarProduto(View b) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Você realmente deseja apagar o produto: \"" + no + "\" ?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        voltar();
                    }

                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MostrarProduto.this, "Procedimento cancelado pelo usuário!", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }

    private void voltar() {
        Conexao con = new Conexao(this);
        if (con.apagaProduto(no)) {
            setResult(Activity.RESULT_OK, new Intent());
            Toast.makeText(this, "Produto: \"" + no + "\"\nFoi apagado com sucesso!", Toast.LENGTH_LONG).show();
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Não foi possivel apagar o produto \"" + no + "\"!\nPor favor, tente novamente!", Toast.LENGTH_LONG).show();
        }


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
