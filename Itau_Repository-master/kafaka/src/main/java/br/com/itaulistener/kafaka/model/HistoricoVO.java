package br.com.itaulistener.kafaka.model;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;


@Table(value = "historico")
	public class HistoricoVO implements Serializable{
	    /**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@PrimaryKeyColumn(
				name = "id",
				ordinal = 1,
				type = PrimaryKeyType.PARTITIONED
		)
		private UUID id;
		@Column(value = "num_conta")
		private String num_conta;
	    @Column(value = "tipo_de_transacao")
	    String tipo_de_transacao;
	    @Column(value = "status")
	    int status;
	    @Column(value = "data")
		Timestamp data;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getData() {
		return data;
	}

	public void setData(Timestamp data) {
		this.data = data;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
}

