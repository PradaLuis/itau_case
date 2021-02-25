package br.com.uscs.uscsitau.errorhandling;

public enum ErrorCode {

    CPF_CNPJ_ALREADY_EXISTS(1000, "CPF / CNPJ ja existe"),
    CPF_CNPJ_CANNOT_BE_NULL_OR_EMPTY(1001, "CPF / CNPJ nao pode ser nulo ou vazio"),
    CPF_CNPJ_NOT_FOUND(1002, "CPF / CNPJ nao existe"),
    CPF_CNPJ_INVALID(1003, "CPF / CNPJ Invalido"),
    ACCOUNT_NOT_FOUND(1004, "Conta nao existente"),
    INSUFFICIENT_FUNDS(1005, "Saldo insuficiente"),
    BAD_REQUEST(204, "Erro ao executar");

    private Integer code;
    private String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
