package br.com.fagnerabsynth.aplicativo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;

import br.com.fagnerabsynth.aplicativo.Views.Login;
import br.com.fagnerabsynth.aplicativo.Views.Principal;

public class Inicial extends Activity {

    //Coloca em um metodo statico o valor da sessao para consulta
    private static boolean sessao = false;
    private String valor;
    public static boolean sessao() {
        return sessao;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicial);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("sessao", 0);
        valor = pref.getString("sessao", null);

        sessao = !TextUtils.isEmpty(valor);


        if (!sessao) {
//faz uma graça de mostrar o activityindicator
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    inicia();
                }
            }, 3000);

        } else {
            inicia();
        }

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

    private void inicia() {


        //verifica se a sessao com o nome da pessoa esta ativa
        String valor;


        //verifica se a sessao esta ativa ou nao e direciona para a pagina correta!
        Intent intentado;

        if (!sessao) {
            intentado = new Intent(this, Login.class);
        } else {
            intentado = new Intent(this, Principal.class);
        }

        startActivity(intentado);

        //impede do usuario retornar a pagina anterior
        finish();
    }


}
