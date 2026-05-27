package model;

import enums.StatusEstacao;

public class EstacaoRecarga {

    private int id;
    private String localizacao;
    private double potencia;
    private StatusEstacao status;

    public EstacaoRecarga(int id, String localizacao, double potencia) {
        this.id = id;
        this.localizacao = localizacao;
        this.potencia = potencia;
        this.status = StatusEstacao.DISPONIVEL;
    }

    public StatusEstacao getStatus() {
        return status;
    }

    public void ocuparEstacao() {
        status = StatusEstacao.OCUPADA;
    }

    public void liberarEstacao() {
        status = StatusEstacao.DISPONIVEL;
    }

    @Override
    public String toString() {
        return "Estação " + id + " | Local: " + localizacao + " | Potência: " + potencia + "kW" + " | Status: " + status;
    }
}