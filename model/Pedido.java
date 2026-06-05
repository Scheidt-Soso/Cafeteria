package model;

import java.sql.Timestamp;

public class Pedido {

    private int id;
    private int clienteId;
    private StatusEnum status;
    private Timestamp dataCriacao;

    public Pedido() {
    }

    public Pedido(int clienteId, StatusEnum status) {
        this.clienteId = clienteId;
        this.status = status;
    }

    public Pedido(int id, int clienteId, StatusEnum status, Timestamp dataCriacao) {
        this.id = id;
        this.clienteId = clienteId;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public Timestamp getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Timestamp dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
