package model;

import exceptions.SaldoInsuficienteException;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

    private double saldo;

    private List<VeiculoEletrico> veiculos;

    private List<SessaoRecarga> historicoRecargas = new ArrayList<>();

    public Cliente(String nome, String email, String senha, double saldo) {

        super(nome, email, senha);

        this.saldo = saldo;

        this.veiculos = new ArrayList<>();
    }

    public double getSaldo() {
        return saldo;
    }

    public void adicionarSaldo(double valor) {

        saldo += valor;

        System.out.println("Saldo adicionado com sucesso.");
    }

    public List<VeiculoEletrico> getVeiculos() {
        return veiculos;
    }

    public void descontarSaldo(double valor) throws SaldoInsuficienteException {

        if (valor > saldo) {

            throw new SaldoInsuficienteException("Saldo insuficiente para realizar pagamento.");
        }

        saldo -= valor;

        System.out.println("Pagamento realizado com sucesso.");
    }

    public void cadastrarVeiculo(VeiculoEletrico veiculo) {

        veiculos.add(veiculo);

        System.out.println("Veículo cadastrado com sucesso.");
    }

    public void adicionarHistorico(SessaoRecarga sessao) {

        historicoRecargas.add(sessao);
    }

    public List<SessaoRecarga> getHistoricoRecargas() {

        return historicoRecargas;
    }

    @Override
    public void exibirMenu() {

        System.out.println("=== MENU CLIENTE ===");
    }
}