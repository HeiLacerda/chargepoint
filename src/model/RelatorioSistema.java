package model;

import java.util.List;

public class RelatorioSistema {

    public void gerarRelatorioEstacoes(List<EstacaoRecarga> estacoes) {

        System.out.println("\n=== RELATÓRIO DE ESTAÇÕES ===");

        for (EstacaoRecarga e : estacoes) {

            System.out.println(e);
        }
    }

    public void gerarRelatorioClientes(List<Cliente> clientes) {

        System.out.println("\n=== RELATÓRIO DE CLIENTES ===");

        for (Cliente c : clientes) {

            System.out.println("Nome: " + c.getNome());

            System.out.println("Saldo: R$ " + c.getSaldo());

            System.out.println("-------------------");
        }
    }
}