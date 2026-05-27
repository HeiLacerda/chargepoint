package model;

import enums.TipoPagamento;
import interfaces.Pagavel;

public class Pagamento implements Pagavel {

    private TipoPagamento tipoPagamento;

    public Pagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    @Override
    public void realizarPagamento(double valor) {
        System.out.println("Pagamento realizado via " + tipoPagamento);
        System.out.println("Valor pago: R$ " + valor);
    }
}