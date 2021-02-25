package br.com.uscs.uscsitau.model;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Table(value = "conta")
public class ContaVO implements Serializable{

	private static final long serialVersionUID = 1L;

    @PrimaryKeyColumn(
            name = "num_conta",
            ordinal = 1,
            type = PrimaryKeyType.PARTITIONED
    )
    String num_conta;
	@Column(value = "agencia")
    String agencia;
	@Column(value = "dac")
    int dac;
	@Column(value = "saldo")
    double saldo;

	public String getNum_conta() {
		return num_conta;
	}

	public void setNum_conta(String num_conta) {
		this.num_conta = num_conta;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public int getDac() {
		return dac;
	}

	public void setDac(int dac) {
		this.dac = dac;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
}
