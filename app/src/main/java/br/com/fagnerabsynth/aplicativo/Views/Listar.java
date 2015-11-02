package br.com.fagnerabsynth.aplicativo.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.fagnerabsynth.aplicativo.Data.Conexao;
import br.com.fagnerabsynth.aplicativo.Models.App;
import br.com.fagnerabsynth.aplicativo.Models.ProdutosAdapter;
import br.com.fagnerabsynth.aplicativo.Models.ProdutosMOD;
import br.com.fagnerabsynth.aplicativo.R;

public class Listar extends AppCompatActivity {
    protected static final int CONTEXTMENU_OPTION1 = 1;
    protected static final int CONTEXTMENU_OPTION2 = 2;
    protected static final int CONTEXTMENU_OPTION3 = 2;
    private ListView listView;
    private EditText pesquisar, et;
    private List<ProdutosMOD> lista;
    private Conexao con;
    private String tx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar);

        String TITULO = new App().getNome();
        String SUBTITULO = "pesquisar";
        getSupportActionBar().setTitle(TITULO);
        getSupportActionBar().setSubtitle(SUBTITULO);

        et = (EditText) findViewById(R.id.procurar);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                iniciar();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        iniciar();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Selecione:");
        menu.add(Menu.NONE, CONTEXTMENU_OPTION1, 0, "Alterar produto");
        menu.add(Menu.NONE, CONTEXTMENU_OPTION2, 1, "Apagar produto");
        menu.add(Menu.NONE, CONTEXTMENU_OPTION3, 2, "Cancelar");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int id = menuInfo.position;

        View rowView = listView.getChildAt(id);
        TextView textview = (TextView) rowView.findViewById(R.id.nome);
        String nome = textview.getText().toString();

        switch (item.getItemId()) {

            case CONTEXTMENU_OPTION1:
                if (con.apagaProduto(nome)) {
                    Toast.makeText(this, "O produto selecionado: \"" + nome + "\"\nfoi removido com sucesso!", Toast.LENGTH_LONG).show();
                    iniciar();

                } else {
                    Toast.makeText(this, "Não foi possivel apagar produto selecionado: \"" + nome + "\"\n\nPor favor, tente novamente!", Toast.LENGTH_LONG).show();
                }
                break;
            case CONTEXTMENU_OPTION2:
                Intent intentado = new Intent(this, Edicao.class);
                intentado.putExtra("nome", nome);
                startActivity(intentado);
                break;

        }

        return true;
    }


    public void iniciar() {

        tx = et.getText().toString();

        con = new Conexao(this);

        if (TextUtils.isEmpty(tx)) {
            lista = con.pesquisaProduto();
        } else {
            lista = con.pesquisaProduto(tx);
        }

        ProdutosAdapter adapter = new ProdutosAdapter(this, lista);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View rowView = listView.getChildAt(position);
                TextView textview = (TextView) rowView.findViewById(R.id.nome);
                String nome = textview.getText().toString();
                mudaPagina(nome);
            }
        });


        registerForContextMenu(listView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                iniciar();
            }
        }
    }

    private void mudaPagina(String nome) {
        Intent in = new Intent(this, MostrarProduto.class);
        in.putExtra("nome", nome);
        startActivityForResult(in, 1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        //  getMenuInflater().inflate(R.menu.listar, menu);

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