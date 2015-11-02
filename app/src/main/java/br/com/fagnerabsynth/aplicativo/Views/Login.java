package br.com.fagnerabsynth.aplicativo.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.fagnerabsynth.aplicativo.Data.Conexao;
import br.com.fagnerabsynth.aplicativo.Inicial;
import br.com.fagnerabsynth.aplicativo.Models.App;
import br.com.fagnerabsynth.aplicativo.R;

public class Login extends AppCompatActivity {

    private EditText txtMail, txtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        txtMail = (EditText) findViewById(R.id.txtMail);
        txtPass = (EditText) findViewById(R.id.txtPass);


        String SUBTITULO = "For favor, faça seu login!";
        String TITULO = new App().getNome();
        getSupportActionBar().setTitle(TITULO);
        getSupportActionBar().setSubtitle(SUBTITULO);
        getSupportActionBar().setIcon(R.mipmap.logo);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void logar(View btn) {

        String mail, pass;

        mail = txtMail.getText().toString();

        pass = txtPass.getText().toString();

        String erro = "";

        if (TextUtils.isEmpty(mail)) {
            erro += "O Campo email não pode ficar em branco!";
        } else if (!mail.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
            txtMail.setText("");

            if (erro != "") {
                erro += "\n";
            }

            erro += "O email é inválido!";
        }

        if (TextUtils.isEmpty(pass)) {
            if (erro != "") {
                erro += "\n";
            }
            erro += "O Campo senha não pode ficar em branco!";
        }

        if (erro == "") {
            Conexao log = new Conexao(this);
            if (!log.logar(mail, pass)) {
                erro += "Dados inválido, tente novamente!";
            }

        }


        if (erro != "") {
            Toast.makeText(Login.this, erro, Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("sessao", 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("sessao", mail);
            editor.commit();
            Toast.makeText(Login.this, "Seja bem vindo ao aplicativo \"" + new App().getNome() + "\"!\n" + mail, Toast.LENGTH_LONG).show();

            Intent intentado = new Intent(this, Inicial.class);
            startActivity(intentado);
            finish();


        }


    }


    public void fechar(View b) {
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
