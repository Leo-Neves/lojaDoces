package br.ufpa.lojadoces.model;

import java.util.Date;
import java.util.List;

public class Pedido {
    private String _id;
    private Date dataPedido;
    private List<Produto> produtos;
    private Usuario usuario;
    private Ponto pontuacaoGerada;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Ponto getPontuacaoGerada() {
        return pontuacaoGerada;
    }

    public void setPontuacaoGerada(Ponto pontuacaoGerada) {
        this.pontuacaoGerada = pontuacaoGerada;
    }
}
