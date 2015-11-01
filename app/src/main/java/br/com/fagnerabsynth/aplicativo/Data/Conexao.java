package br.com.fagnerabsynth.aplicativo.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import br.com.fagnerabsynth.aplicativo.Models.ProdutosMOD;

/**
 * Created by fagner on 29/10/15.
 */


public class Conexao extends SQLiteOpenHelper {
    public static final String TABELA = "usuarios";
    public static final String TABELA2 = "produtos";
    public static final String TABELA3 = "categoria";

    private static final String ARQUIVO = "usuarios.db";

    private static final int VERSAO = 1;

    private static final String CRIARTABELA1 = "create table "
            + TABELA + "( id integer primary key autoincrement, email text not null, senha text not null);";

    private static final String CRIARTABELA2 = "create table "
            + TABELA2 + "( id integer primary key autoincrement, nome text not null, categoria text not null,descricao text not null, ativo flag INTEGER DEFAULT 0, valor text not null );";

    private static final String CRIARTABELA3 = "create table "
            + TABELA3 + "( id integer primary key autoincrement, categoria text not null unique);";


    private SQLiteDatabase db;

    public Conexao(Context context) {
        super(context, ARQUIVO, null, VERSAO);
    }

    private String md5(String s) {
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
        db.execSQL(CRIARTABELA2);
        db.execSQL(CRIARTABELA3);


        Cursor rs = db.rawQuery("SELECT * FROM " + TABELA, null);

        if (rs.getCount() == 0) {
            String[] dadosAdmin = {"admin@admin.com", md5("12345")};
            db.execSQL("Insert into " + TABELA + "(email,senha) values (?,?)", dadosAdmin);
        }

        String temp = "";
        Cursor tb2 = db.rawQuery("SELECT * FROM " + TABELA2, null);
        if (tb2.getCount() > 0) {

            if (tb2.moveToFirst()) {
                do {
                    temp = tb2.getString(tb2.getColumnIndex("categoria"));
                    Cursor tb3 = db.rawQuery("SELECT * FROM " + TABELA3 + " where categoria = ?", new String[]{temp});
                    if (tb3.getCount() == 0) {
                        db.execSQL("DELETE  FROM " + TABELA3 + " where categoria = ?", new String[]{temp});
                    }
                } while (tb2.moveToNext());
            }
        }


    }


    public boolean adicionaProduto(ProdutosMOD produto) {
        db = this.getWritableDatabase();

        try {
            if (produto.id != 0) {
                db.execSQL("Insert into " + TABELA2 + "(nome,categoria,descricao,ativo,valor) values (?,?,?,?,?)", new String[]{produto.nome, produto.categoria, produto.descricao, "" + produto.ativo, "" + produto.valor});
            } else {
                db.execSQL("update " + TABELA2 + " set nome=?,categoria=?,descricao=?,ativo=?,valor=? where id = ?", new String[]{produto.nome, produto.categoria, produto.descricao, "" + produto.ativo, "" + produto.valor, "" + produto.id});
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public boolean adicionaCategoria(String cat) {
        db = this.getWritableDatabase();
        try {
            db.execSQL("Insert into " + TABELA3 + " (categoria) values (?)", new String[]{cat});
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public ArrayList<String> selecionaCategorias() {
        ArrayList<String> retorno = new ArrayList<String>();
        db = this.getWritableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM " + TABELA3 + " order by categoria asc", null);
        if (rs.getCount() > 0) {
            if (rs.moveToFirst()) {
                do {
                    retorno.add(rs.getString(rs.getColumnIndex("categoria")));
                } while (rs.moveToNext());
            }
        }
        return retorno;
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