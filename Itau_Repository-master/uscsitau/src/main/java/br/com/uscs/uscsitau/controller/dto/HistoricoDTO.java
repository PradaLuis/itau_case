package br.com.uscs.uscsitau.controller.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class HistoricoDTO {

    private UUID id;
    private String num_conta;
    private String tipo_de_transacao;
    private Date data;
    private Integer status; // Failed = 0, Success = 1

    public String getNum_conta() {
        return num_conta;
    }

    public void setNum_conta(String num_conta) {
        this.num_conta = num_conta;
    }

    public String getTipo_de_transacao() {
        return tipo_de_transacao;
    }

    public void setTipo_de_transacao(String tipo_de_transacao) {
        this.tipo_de_transacao = tipo_de_transacao;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
