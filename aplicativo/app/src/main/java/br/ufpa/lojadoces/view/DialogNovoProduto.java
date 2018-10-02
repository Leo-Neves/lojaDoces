package br.ufpa.lojadoces.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import br.elois.maktaba.dialog.Dialog;
import br.elois.maktaba.dialog.DialogAction;
import br.ufpa.lojadoces.MainActivity;
import br.ufpa.lojadoces.R;

public class DialogNovoProduto {
    private View view;
    private EditText etNome, etValor, etCategoria1, etCategoria2;

    public DialogNovoProduto(MainActivity activity, final ProdutoCadastradoListener produtoCadastradoListener){
        popularCampos(activity);
        Dialog.Builder builder = new Dialog.Builder(activity)
                .customView(view, false)
                .positiveText("Salvar")
                .title("Novo Produto")
                .onPositive(new Dialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull Dialog dialog, @NonNull DialogAction which) {
                        produtoCadastradoListener.quandoCadastrar(etNome.getText().toString(),
                                                                    etValor.getText().toString(),
                                                                    etCategoria1.getText().toString(),
                                                                    etCategoria2.getText().toString());
                    }
                });
        Dialog dialog = new Dialog(builder);
        dialog.show();
    }

    private void popularCampos(MainActivity activity){
        view = activity.getLayoutInflater().inflate(R.layout.dialog_produto, null, false);
        etNome = view.findViewById(R.id.etNomeProduto);
        etValor = view.findViewById(R.id.etValorProduto);
        etCategoria1 = view.findViewById(R.id.etCategoria1Produto);
        etCategoria2 = view.findViewById(R.id.etCategoria2Produto);
    }

    public interface ProdutoCadastradoListener{
        void quandoCadastrar(String nome, String valor, String categoria1, String categoria2);
    }
}
