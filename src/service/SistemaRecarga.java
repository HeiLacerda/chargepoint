package service;

import model.Cliente;
import model.EstacaoRecarga;

import java.util.ArrayList;
import java.util.List;

public class SistemaRecarga {

    private List<Cliente> clientes;

    private List<EstacaoRecarga> estacoes;

    public SistemaRecarga() {

        clientes = new ArrayList<>();

        estacoes = new ArrayList<>();
    }

    public void cadastrarCliente(Cliente cliente) {

        clientes.add(cliente);

        System.out.println("Cliente cadastrado com sucesso.");
    }

    public void cadastrarEstacao(EstacaoRecarga estacao) {

        estacoes.add(estacao);

        System.out.println("Estação cadastrada com sucesso.");
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<EstacaoRecarga> getEstacoes() {
        return estacoes;
    }

    public void listarEstacoes() {

        System.out.println("\n=== ESTAÇÕES CADASTRADAS ===");

        for (EstacaoRecarga e : estacoes) {

            System.out.println(e);
        }
    }
}