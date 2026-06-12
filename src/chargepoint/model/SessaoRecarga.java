package chargepoint.model;

import chargepoint.enums.StatusSessao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SessaoRecarga {

    private VeiculoEletrico veiculo;
    private EstacaoRecarga estacao;
    private double energiaConsumida;
    private double valorTotal;
    private LocalDateTime tempoInicio;
    private StatusSessao statusSessao;
    private EstrategiaRecarga estrategia;

    public SessaoRecarga(VeiculoEletrico veiculo, EstacaoRecarga estacao, EstrategiaRecarga estrategia) {
        this.veiculo = veiculo;
        this.estacao = estacao;
        this.estrategia = estrategia;
        this.tempoInicio = LocalDateTime.now();
        this.statusSessao = StatusSessao.ATIVA;
        this.energiaConsumida = 0;
    }

    public boolean tick() {
        if (statusSessao != StatusSessao.ATIVA) return false;
        if (veiculo.bateriaCheinha()) return false;

        boolean continuando = veiculo.receberUmKwh();
        energiaConsumida += 1.0;
        imprimirBarra();

        if (!continuando || veiculo.bateriaCheinha()) {
            finalizarSessao();
            return false;
        }
        return true;
    }

    private void imprimirBarra() {
        double pct = veiculo.getPercentualBateria();
        int filled = (int) (pct / 5);
        int empty = 20 - filled;

        StringBuilder barra = new StringBuilder();
        barra.append("\r  [");
        for (int i = 0; i < filled; i++) barra.append("█");
        for (int i = 0; i < empty; i++) barra.append("░");
        barra.append(String.format("] %5.1f%%  %.0f/%.0f kWh  —  %s", pct, veiculo.getNivelBateria(), veiculo.getCapacidadeBateria(), veiculo.getModelo()));

        System.out.print(barra);
    }

    public void finalizarSessao() {
        if (statusSessao == StatusSessao.FINALIZADA) return;
        statusSessao = StatusSessao.FINALIZADA;
        valorTotal = estrategia.calcularPreco(energiaConsumida);
        System.out.println();
        exibirResumo();
    }

    public void cancelarSessao() {
        statusSessao = StatusSessao.CANCELADA;
        System.out.println("\n  [SESSÃO] Cancelada: " + veiculo.getModelo());
    }

    public void exibirResumo() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("  ||======================================================");
        System.out.println("  || RESUMO DA SESSÃO");
        System.out.printf("  || Veículo    : %s%n", veiculo.getModelo());
        System.out.printf("  || Estratégia : %s%n", estrategia.getNome());
        System.out.printf("  || Início     : %s%n", tempoInicio.format(fmt));
        System.out.printf("  || Energia    : %.1f kWh%n", energiaConsumida);
        System.out.printf("  || Valor      : R$ %.2f%n", valorTotal);
        System.out.printf("  || Status     : %s%n", statusSessao);
        System.out.println("  ||======================================================");
    }

    public StatusSessao getStatusSessao() {
        return statusSessao;
    }

    public double getEnergiaConsumida() {
        return energiaConsumida;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public VeiculoEletrico getVeiculo() {
        return veiculo;
    }
}