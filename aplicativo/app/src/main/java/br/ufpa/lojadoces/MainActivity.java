package br.ufpa.lojadoces;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import br.elois.maktaba.dialog.Dialog;
import br.elois.maktaba.dialog.sweet.SweetAlertDialog;
import br.ufpa.lojadoces.connection.EnvioJson;
import br.ufpa.lojadoces.connection.EnvioListener;
import br.ufpa.lojadoces.connection.log.Logger;
import br.ufpa.lojadoces.model.Produto;
import br.ufpa.lojadoces.view.DialogNovoProduto;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final RecyclerView recyclerView = findViewById(R.id.rvProdutos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogNovoProduto(MainActivity.this, new DialogNovoProduto.ProdutoCadastradoListener() {
                    @Override
                    public void quandoCadastrar(String nome, String valor, String categoria1, String categoria2) {
                        try {
                            double valorDouble = Double.valueOf(valor);
                            JSONArray categorias = new JSONArray();
                            categorias.put(categoria1);
                            categorias.put(categoria2);
                            JSONObject body = new JSONObject();
                            body.put("nome", nome);
                            body.put("valor", valorDouble);
                            body.put("categorias", categorias);
                            EnvioJson envioJson = new EnvioJson(Request.Method.POST, "http://192.168.1.105:3000/produtos", body, null, new EnvioListener() {
                                @Override
                                public void onSuccess(JSONArray result) {

                                }
                            });
                            envioJson.sync(MainActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        Logger.init();
        EnvioJson envioJson = new EnvioJson(Request.Method.GET, "http://192.168.1.105:3000/produtos", null, null, new EnvioListener() {
            @Override
            public void onSuccess(JSONArray result) {
                List<Produto> produtos = new ArrayList<>();
                for (int i=0;i<result.length();i++){
                    try {
                        JSONObject object = result.getJSONObject(i);
                        Produto produto = new Produto();
                        produto.set_id(object.getString("_id"));
                        produto.setNome(object.getString("nome"));
                        produto.setValor(object.getDouble("valor"));
                        JSONArray categorias = object.getJSONArray("categorias");
                        List<String> cat = new ArrayList<>();
                        for (int j=0;j<categorias.length();j++){
                            cat.add(categorias.getString(j));
                        }
                        produto.setCategorias(cat);
                        produtos.add(produto);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ProdutosAdapter adapter = new ProdutosAdapter(MainActivity.this, produtos);
                recyclerView.setAdapter(adapter);
            }
        });
        envioJson.sync(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
