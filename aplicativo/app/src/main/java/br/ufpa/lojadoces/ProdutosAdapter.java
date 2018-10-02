package br.ufpa.lojadoces;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import br.ufpa.lojadoces.model.Produto;

public class ProdutosAdapter extends RecyclerView.Adapter<ProdutosAdapter.ViewHolder> {
    private List<Produto> produtos;
    private MainActivity activity;

    public ProdutosAdapter(MainActivity activity, List<Produto> produtos){
        this.activity = activity;
        this.produtos = produtos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(activity.getLayoutInflater().inflate(R.layout.row_produto, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Produto produto = produtos.get(i);
        holder.categorias.setText(produto.getCategorias().toString());
        holder.nome.setText(produto.getNome());
        holder.valor.setText(String.valueOf(produto.getValor()));
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nome;
        public TextView valor;
        public TextView categorias;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tvNomeProduto);
            valor = itemView.findViewById(R.id.tvPrecoProduto);
            categorias = itemView.findViewById(R.id.tvCategoriaProduto);
        }
    }
}
