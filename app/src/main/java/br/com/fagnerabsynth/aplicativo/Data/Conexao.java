package br.com.fagnerabsynth.aplicativo.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
            + TABELA2 + "( id integer primary key autoincrement, nome text not null unique, categoria text not null,descricao text not null, ativo flag INTEGER DEFAULT 0, valor text not null );";

    private static final String CRIARTABELA3 = "create table "
            + TABELA3 + "( id integer primary key autoincrement, categoria text not null unique);";


    private SQLiteDatabase db;

    private String pesquisa = "";


    public Conexao(Context context) {
        super(context, ARQUIVO, null, VERSAO);
    }

    public ProdutosMOD pesquisaProduto(String nome, int b) {
        db = this.getWritableDatabase();
        Cursor tb = db.rawQuery("select * from " + TABELA2 + " where ativo = '1' and nome = ? order by nome asc ", new String[]{nome});
        ProdutosMOD objeto = new ProdutosMOD();
        if (tb.getCount() > 0) {
            if (tb.moveToFirst()) {
                objeto.nome = tb.getString(tb.getColumnIndex("nome"));
                objeto.categoria = tb.getString(tb.getColumnIndex("categoria"));
                objeto.valor = tb.getString(tb.getColumnIndex("valor"));
                objeto.id = Integer.parseInt(tb.getString(tb.getColumnIndex("id")));
                objeto.descricao = tb.getString(tb.getColumnIndex("descricao"));
                objeto.ativo = Integer.parseInt(tb.getString(tb.getColumnIndex("ativo")));
            }
        }
        return objeto;
    }

    public List<ProdutosMOD> pesquisaProduto(String pesquisa) {
        this.pesquisa = pesquisa;
        List<ProdutosMOD> dados = pesquisaProduto();
        this.pesquisa = "";
        return dados;
    }

    public List<ProdutosMOD> pesquisaProduto() {
        db = this.getWritableDatabase();
        List<ProdutosMOD> lista = new ArrayList<ProdutosMOD>();
        String query = "";
        Cursor tb;
        if (TextUtils.isEmpty(pesquisa))
            tb = db.rawQuery("select * from " + TABELA2 + " where ativo = '1' order by nome asc ", null);
        else
            tb = db.rawQuery("select * from " + TABELA2 + " where nome like ? and  ativo = '1' order by nome asc ", new String[]{"%" + pesquisa + "%"});


        ProdutosMOD objeto;
        if (tb.getCount() > 0) {
            if (tb.moveToFirst()) {
                do {
                    objeto = new ProdutosMOD();
                    objeto.nome = tb.getString(tb.getColumnIndex("nome"));
                    objeto.categoria = tb.getString(tb.getColumnIndex("categoria"));
                    objeto.valor = tb.getString(tb.getColumnIndex("valor"));
                    objeto.id = Integer.parseInt(tb.getString(tb.getColumnIndex("id")));
                    objeto.descricao = tb.getString(tb.getColumnIndex("descricao"));
                    objeto.ativo = Integer.parseInt(tb.getString(tb.getColumnIndex("ativo")));
                    lista.add(objeto);
                } while (tb.moveToNext());
            }
        } else {
            lista = new ArrayList<ProdutosMOD>();
        }
        return lista;
    }

    public void limpa() {
        db = this.getWritableDatabase();
        String resultado;
        Cursor tb2 = db.rawQuery("SELECT * FROM " + TABELA3 + " ", null);
        if (tb2.getCount() > 0) {
            if (tb2.moveToFirst()) {
                do {
                    resultado = tb2.getString(tb2.getColumnIndex("categoria"));
                    if (db.rawQuery("SELECT * FROM " + TABELA2 + " where categoria = ? ", new String[]{resultado}).getCount() == 0) {
                        db.execSQL("DELETE FROM " + TABELA3 + " where categoria = ?", new String[]{resultado});
                    }
                } while (tb2.moveToNext());
            }
        }
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
    }


    public boolean apagaProduto(String nome) {
        db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + TABELA2 + " where nome = ?", new String[]{nome});
        } catch (Exception e) {
            return false;
        }
        return true;
    }



    public boolean adicionaProduto(ProdutosMOD produto) {
        db = this.getWritableDatabase();

        try {
            if (produto.id == 0) {
                db.execSQL("Insert into " + TABELA2 + "(nome,categoria,descricao,ativo,valor) values (?,?,?,?,?)", new String[]{produto.nome, produto.categoria, produto.descricao, "" + produto.ativo, "" + produto.valor});
            } else {
                db.execSQL("update " + TABELA2 + " set nome=?,categoria=?,descricao=?,ativo=?,valor=? where id = ?", new String[]{produto.nome, produto.categoria, produto.descricao, "" + produto.ativo, produto.valor, "" + produto.id});
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