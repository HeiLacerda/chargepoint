package model;

import exceptions.EstacaoIndisponivelException;

public class SessaoRecarga {

    private Cliente cliente;
    private VeiculoEletrico veiculo;
    private EstacaoRecarga estacao;

    private double energiaConsumida;
    private double valorTotal;

    public SessaoRecarga(Cliente cliente, VeiculoEletrico veiculo, EstacaoRecarga estacao) throws EstacaoIndisponivelException {

        if (estacao.getStatus().toString().equals("OCUPADA")) {

            throw new EstacaoIndisponivelException("A estação está ocupada.");
        }

        this.cliente = cliente;
        this.veiculo = veiculo;
        this.estacao = estacao;

        estacao.ocuparEstacao();
    }

    public boolean iniciarRecarga(double kwh) {

        if (!estacao.podeCarregar(veiculo, kwh)) {

            return false;
        }

        energiaConsumida = kwh;

        valorTotal = energiaConsumida * 2.50;

        veiculo.carregar(kwh);

        estacao.fornecerEnergia(kwh);

        cliente.adicionarHistorico(this);

        estacao.adicionarHistorico(veiculo);

        System.out.println("Recarga iniciada...");

        return true;
    }

    public void encerrarRecarga() {

        estacao.liberarEstacao();

        System.out.println("Recarga encerrada.");
        System.out.println("Energia consumida: " + energiaConsumida + " kWh");

        System.out.println("Valor total: R$ " + valorTotal);
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public VeiculoEletrico getVeiculo() {
        return veiculo;
    }

    public EstacaoRecarga getEstacao() {
        return estacao;
    }

    public double getEnergiaConsumida() {
        return energiaConsumida;
    }
}