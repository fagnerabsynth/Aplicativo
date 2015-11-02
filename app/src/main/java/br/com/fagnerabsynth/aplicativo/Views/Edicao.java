package br.com.fagnerabsynth.aplicativo.Views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.fagnerabsynth.aplicativo.Data.Conexao;
import br.com.fagnerabsynth.aplicativo.Models.App;
import br.com.fagnerabsynth.aplicativo.Models.ProdutosMOD;
import br.com.fagnerabsynth.aplicativo.R;

public class Edicao extends AppCompatActivity {
    private Conexao con;
    private Spinner spinner;
    private String valorSpinner, add = "Adicionar nova categoria";
    private LinearLayout linear, popup;
    private RelativeLayout main;
    private ProdutosMOD pro = new ProdutosMOD();
    private EditText Lblproduto, LblDesc, Lblpreco;
    private RadioButton sim, nao;

    public void cancelar(View v) {
        linear.setVisibility(View.VISIBLE);
        popup.setVisibility(View.INVISIBLE);
        Toast.makeText(Edicao.this, "Cadastro de nova categoria\ncancelado pelo usuário", Toast.LENGTH_SHORT).show();
        main.setBackgroundColor(Color.WHITE);
        criaSpinner();
    }

    public void listarProduto(View btn) {
        Intent intentado = new Intent(this, Listar.class);
        startActivity(intentado);
    }

    public void cadastraCategoria(View b) {
        EditText input = (EditText) findViewById(R.id.novaCategoria);
        String novaCategoria = input.getText().toString();
        String mensagem = "";

        if (TextUtils.isEmpty(novaCategoria)) {
            mensagem = "Digite o nome da categoria";

        } else {

            try {
                if (con.adicionaCategoria(novaCategoria)) {
                    mensagem = "Categoria adicionada com sucesso!";
                    linear.setVisibility(View.VISIBLE);
                    popup.setVisibility(View.INVISIBLE);
                    main.setBackgroundColor(Color.WHITE);
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

    public void cadastraProduto(View v) {
        String produto = Lblproduto.getText().toString();
        String descricao = LblDesc.getText().toString();
        String preco = Lblpreco.getText().toString();

        boolean radio = false;


        if (sim.isChecked() || nao.isChecked()) {
            radio = true;
        }

        String erro = "";
        if (TextUtils.isEmpty(valorSpinner) || valorSpinner.equals(add)) {
            erro = "Selecione uma categoria válida";
        }


        int ativo = 0;

        if (!radio) {
            if (!erro.equals("")) {
                erro += "\n";
            }
            erro += "Selecione uma opção no menu de ativação";
        } else {
            if (sim.isChecked()) {
                ativo = 1;
            }
        }


        if (TextUtils.isEmpty(produto)) {
            if (!erro.equals("")) {
                erro += "\n";
            }
            erro += "O Produto não pode ficar em branco";
        }


        if (TextUtils.isEmpty(descricao)) {
            if (!erro.equals("")) {
                erro += "\n";
            }
            erro += "A Descrição não pode ficar em branco";
        }


        if (TextUtils.isEmpty(preco)) {
            if (!erro.equals("")) {
                erro += "\n";
            }
            erro += "O Preço não pode ficar em branco";
        }


        if (!erro.equals("")) {
            Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();
        } else {
            if (pro.id == 0) {
                pro = new ProdutosMOD();
                pro.id = 0;
                pro.ativo = ativo;
                pro.categoria = valorSpinner.toString();
                pro.descricao = descricao;
                pro.nome = produto;
                pro.valor = preco;

                if (con.adicionaProduto(pro)) {
                    sim.setChecked(false);
                    nao.setChecked(false);
                    LblDesc.setText("");
                    Lblpreco.setText("");
                    Lblproduto.setText("");
                    spinner.setSelection(0);

                    Toast.makeText(this, "Produto adicionado com sucesso!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Não foi possivel cadastrar o produto!\n" +
                            "Produto com o mesmo nome existente!", Toast.LENGTH_LONG).show();
                }
            } else {
                pro.ativo = ativo;
                pro.categoria = valorSpinner.toString();
                pro.descricao = descricao;
                pro.nome = produto;
                pro.valor = preco;
                if (con.adicionaProduto(pro)) {
                    setResult(Activity.RESULT_OK, new Intent());
                    Toast.makeText(this, "Produto: \"" + pro.nome + "\" atualizado com sucesso!", Toast.LENGTH_LONG).show();
                    super.onBackPressed();
                } else {
                    Toast.makeText(this, "Não foi possivel atualizar o produto!\nProduto com o mesmo nome existente!", Toast.LENGTH_LONG).show();
                }
            }

        }
        
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
        pro.id = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicao);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        con = new Conexao(this);
        //limpa categoria nao utilizada
        con.limpa();

        sim = (RadioButton) findViewById(R.id.sim);
        nao = (RadioButton) findViewById(R.id.nao);
        Lblproduto = (EditText) findViewById(R.id.produto);
        LblDesc = (EditText) findViewById(R.id.descricao);
        Lblpreco = (EditText) findViewById(R.id.preco);
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

        String nome = getIntent().getStringExtra("nome");


        String SUBTITULO;
        if (TextUtils.isEmpty(nome)) {

            View v = this.findViewById(R.id.cadastra);
            v.setVisibility(View.VISIBLE);

            View v2 = this.findViewById(R.id.altera);
            v2.setVisibility(View.INVISIBLE);

            SUBTITULO = "Adicionar produto";
        } else {
            pro = con.pesquisaProduto(nome, 0);
            Lblproduto.setText(pro.nome);
            LblDesc.setText(pro.descricao);
            Lblpreco.setText(pro.valor);
            spinner.setSelection(((ArrayAdapter<String>) spinner.getAdapter()).getPosition(pro.categoria));
            if (pro.ativo == 1) {
                sim.setChecked(true);
            } else {
                nao.setChecked(true);
            }

            View v = this.findViewById(R.id.cadastra);
            v.setVisibility(View.INVISIBLE);

            View v2 = this.findViewById(R.id.altera);
            v2.setVisibility(View.VISIBLE);

            SUBTITULO = "Alterar: " + pro.nome;
        }

        String TITULO = new App().getNome();
        getSupportActionBar().setTitle(TITULO);
        getSupportActionBar().setSubtitle(SUBTITULO);
        getSupportActionBar().setIcon(R.mipmap.logo);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
