package br.com.fagnerabsynth.aplicativo.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.fagnerabsynth.aplicativo.Inicial;
import br.com.fagnerabsynth.aplicativo.Models.ProdutosMOD;
import br.com.fagnerabsynth.aplicativo.R;

public class Principal extends AppCompatActivity {
    private ProdutosMOD dados = new ProdutosMOD();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    public void cadastraProduto(View btn) {
        Intent intentado = new Intent(this, Edicao.class);
        intentado.putExtra("id", dados.id);
        startActivity(intentado);
    }



    public void fecharSessao(View btn) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("sessao", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("sessao");
        editor.commit();

        Intent intentado = new Intent(this, Inicial.class);
        startActivity(intentado);
        finish();

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
}
