package chargepoint.model;

import chargepoint.enums.StatusEstacao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CentralRecarga {

    private List<EstacaoRecarga> listaEstacoes;
    private String nomeCentral;

    public CentralRecarga(String nomeCentral) {
        this.nomeCentral = nomeCentral;
        this.listaEstacoes = new ArrayList<>();
    }

    public void adicionarEstacao(EstacaoRecarga estacao) {
        listaEstacoes.add(estacao);
        System.out.printf("  [CENTRAL] Estação %s cadastrada em %s.%n", estacao.getId(), nomeCentral);
    }

    public Optional<EstacaoRecarga> buscarMelhorEstacao(VeiculoEletrico veiculo) {
        return listaEstacoes.stream().filter(e -> e.getStatus() == StatusEstacao.DISPONIVEL).filter(e -> e.getTipoConector() == veiculo.getTipoConector()).filter(e -> e.getEnergiaDisponivel() >= veiculo.getEnergiaParaEncher()).max(Comparator.comparingDouble(EstacaoRecarga::getPotencia).thenComparingDouble(EstacaoRecarga::getEnergiaDisponivel));
    }

    public void conectarVeiculoInteligente(VeiculoEletrico veiculo, EstrategiaRecarga estrategia) {
        System.out.printf("%n[CENTRAL] Buscando melhor estação para %s...%n", veiculo.getModelo());

        Optional<EstacaoRecarga> melhor = buscarMelhorEstacao(veiculo);

        if (melhor.isPresent()) {
            EstacaoRecarga estacao = melhor.get();
            System.out.printf("[CENTRAL] Melhor estação encontrada: %s (%.1f kW, %.1f kWh disp.)%n", estacao.getId(), estacao.getPotencia(), estacao.getEnergiaDisponivel());
            try {
                estacao.iniciarRecarga(veiculo, estrategia);
            } catch (Exception e) {
                System.out.println("[CENTRAL] Erro: " + e.getMessage());
            }
        } else {
            System.out.printf("[CENTRAL] Nenhuma estação disponível para %s. Buscando fila...%n", veiculo.getModelo());

            Optional<EstacaoRecarga> filaEstacao = listaEstacoes.stream().filter(e -> e.getTipoConector() == veiculo.getTipoConector()).filter(e -> e.getStatus() != StatusEstacao.MANUTENCAO).min(Comparator.comparingInt(EstacaoRecarga::getTamanhoFila));

            filaEstacao.ifPresent(e -> e.adicionarFila(veiculo));

            if (filaEstacao.isEmpty()) {
                System.out.println("[CENTRAL] Nenhuma estação compatível disponível no momento.");
            }
        }
    }

    public void monitorarEstacoes() {
        System.out.println("\n||======================================================");
        System.out.printf("||  MONITORAMENTO — %s%n", nomeCentral);
        System.out.println("||======================================================");
        System.out.printf("||  %-10s %-15s %-12s %-10s %-8s%n", "ID", "Status", "Energia(kWh)", "Potência", "Fila");
        System.out.println("||  ======================================================");

        for (EstacaoRecarga e : listaEstacoes) {
            System.out.printf("||  %-10s %-15s %-12.1f %-10.1f %-8d%n", e.getId(), e.getStatus(), e.getEnergiaDisponivel(), e.getPotencia(), e.getTamanhoFila());
        }
        System.out.println("||======================================================");
    }

    public void verificarSobrecarga() {
        System.out.println("\n[CENTRAL] Verificando sobrecarga do sistema...");
        long ocupadas = listaEstacoes.stream().filter(e -> e.getStatus() == StatusEstacao.OCUPADA).count();
        long total = listaEstacoes.size();
        double percentual = (double) ocupadas / total * 100;

        System.out.printf("[CENTRAL] %d/%d estações ocupadas (%.0f%%)%n", ocupadas, total, percentual);

        if (percentual >= 80) {
            System.out.println("[CENTRAL] ⚠ ALERTA: Sistema com alta demanda! Considere ativar mais estações.");
        } else if (percentual >= 50) {
            System.out.println("[CENTRAL] Demanda moderada. Sistema estável.");
        } else {
            System.out.println("[CENTRAL] Baixa demanda. Sistema ocioso.");
        }
    }

    public void gerarRelatorioGeral() {
        System.out.println("\n||======================================================");
        System.out.printf("||  RELATÓRIO GERAL — %s%n", nomeCentral);
        System.out.println("||======================================================");

        int totalSessoes = listaEstacoes.stream().mapToInt(e -> e.getHistorico().size()).sum();
        double totalEnergia = listaEstacoes.stream().flatMap(e -> e.getHistorico().stream()).mapToDouble(SessaoRecarga::getEnergiaConsumida).sum();
        double totalReceita = listaEstacoes.stream().flatMap(e -> e.getHistorico().stream()).mapToDouble(SessaoRecarga::getValorTotal).sum();

        System.out.printf("||  Total de estações   : %d%n", listaEstacoes.size());
        System.out.printf("||  Total de sessões    : %d%n", totalSessoes);
        System.out.printf("||  Energia total distr.: %.2f kWh%n", totalEnergia);
        System.out.printf("||  Receita total       : R$ %.2f%n", totalReceita);
        System.out.println("||");

        for (EstacaoRecarga e : listaEstacoes) {
            e.gerarRelatorio();
        }

        System.out.println("||======================================================");
    }

    public List<EstacaoRecarga> getListaEstacoes() {
        return listaEstacoes;
    }

    public String getNomeCentral() {
        return nomeCentral;
    }
}