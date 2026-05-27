package model;

import exceptions.SaldoInsuficienteException;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

    private double saldo;

    private List<VeiculoEletrico> veiculos;

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

    @Override
    public void exibirMenu() {

        System.out.println("=== MENU CLIENTE ===");
    }
}