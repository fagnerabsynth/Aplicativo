package br.com.fagnerabsynth.aplicativo.Views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.fagnerabsynth.aplicativo.Data.Conexao;
import br.com.fagnerabsynth.aplicativo.R;

public class Edicao extends AppCompatActivity {

    private Spinner spinner;
    private String valorSpinner, add = "Adicionar nova categoria";
    private LinearLayout linear, popup;
    private RelativeLayout main;

    public void cancelar(View v) {
        linear.setVisibility(View.VISIBLE);
        popup.setVisibility(View.INVISIBLE);
        Toast.makeText(Edicao.this, "Cadastro de nova categoria\ncancelado pelo usuário", Toast.LENGTH_SHORT).show();
        main.setBackgroundColor(Color.TRANSPARENT);

        criaSpinner();

    }


    public void cadastraCategoria(View b) {

        EditText input = (EditText) findViewById(R.id.novaCategoria);
        String novaCategoria = input.getText().toString();
        String mensagem = "";

        if (TextUtils.isEmpty(novaCategoria)) {
            mensagem = "Digite o nome da categoria";

        } else {

            try {

                Conexao con = new Conexao(this);

                if (con.adicionaCategoria(novaCategoria)) {
                    mensagem = "Categoria adicionada com sucesso!";
                    linear.setVisibility(View.VISIBLE);
                    popup.setVisibility(View.INVISIBLE);
                    main.setBackgroundColor(Color.TRANSPARENT);
                    criaSpinner();
                    input.setText("");
                } else {
                    mensagem = "Erro ao cadastrar categoria\n\nCategoria existente!";
                }

            } catch (Exception e) {
                mensagem = "Não foi possivel adicionar categoria!";
            }
        }
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();

    }

    private void criaSpinner() {
        spinner.setPrompt("Seleciona uma categoria");

        List<String> list = new ArrayList<String>();
        list.add("");

        list.add(add);

        Conexao item = new Conexao(this);

        ArrayList<String> dados = item.selecionaCategorias();
        if (!dados.isEmpty()) {
            for (String x : dados) {
                list.add(x);
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    public void executaSpinner() {

        valorSpinner = spinner.getSelectedItem().toString();
        if (!TextUtils.isEmpty(valorSpinner)) {
            if (valorSpinner.equals(add)) {
                linear.setVisibility(View.INVISIBLE);
                popup.setVisibility(View.VISIBLE);

                main.setBackgroundColor(Color.LTGRAY);
                popup.setBackgroundColor(Color.WHITE);

            }

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicao);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        linear = (LinearLayout) findViewById(R.id.itens);
        popup = (LinearLayout) findViewById(R.id.popup);

        main = (RelativeLayout) findViewById(R.id.main);

        spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                executaSpinner();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        criaSpinner();
        int id = getIntent().getIntExtra("id", 0);

        if (id == 0) {
            setTitle("Adicionar produtos");
        } else {
            setTitle("Alterar produto");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edicao, menu);

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
