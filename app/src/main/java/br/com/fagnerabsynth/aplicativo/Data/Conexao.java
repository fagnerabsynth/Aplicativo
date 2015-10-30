package br.com.fagnerabsynth.aplicativo.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by fagner on 29/10/15.
 */


public class Conexao extends SQLiteOpenHelper {
    public static final String TABELA = "usuarios";
    private static final String ARQUIVO = "usuarios.db";
    private static final int VERSAO = 1;
    private static final String CRIARTABELA1 = "create table "
            + TABELA + "( id integer primary key autoincrement, email text not null, senha text not null);";
    private SQLiteDatabase db;

    public Conexao(Context context) {
        super(context, ARQUIVO, null, VERSAO);
    }

    public String md5(String s) {

        try {

            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CRIARTABELA1);

        Cursor rs = db.rawQuery("SELECT * FROM " + TABELA, null);

        if (rs.getCount() == 0) {
            String[] dadosAdmin = {"admin@admin.com", md5("12345")};
            db.execSQL("Insert into " + TABELA + "(email,senha) values (?,?)", dadosAdmin);
        }
    }


    public boolean logar(String email, String senha) {
        db = this.getWritableDatabase();

        String[] dados = {email, md5(senha)};
        Cursor rs = db.rawQuery("SELECT * FROM " + TABELA + " where email = ? and senha = ?", dados);
        return rs.getCount() == 1;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}