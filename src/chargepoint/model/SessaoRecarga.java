package chargepoint.model;

import chargepoint.enums.StatusSessao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SessaoRecarga {

    private VeiculoEletrico veiculo;
    private EstacaoRecarga estacao;
    private double energiaConsumida; // kWh
    private double valorTotal;
    private LocalDateTime tempoInicio;
    private StatusSessao statusSessao;
    private EstrategiaRecarga estrategia;

    public SessaoRecarga(VeiculoEletrico veiculo, EstacaoRecarga estacao,
                         EstrategiaRecarga estrategia) {
        this.veiculo = veiculo;
        this.estacao = estacao;
        this.estrategia = estrategia;
        this.tempoInicio = LocalDateTime.now();
        this.statusSessao = StatusSessao.ATIVA;
        this.energiaConsumida = 0;
        this.valorTotal = 0;
    }

    public void finalizarSessao(double energiaFornecida) {
        if (statusSessao != StatusSessao.ATIVA) {
            System.out.println("  [SESSÃO] Esta sessão já foi encerrada.");
            return;
        }
        this.energiaConsumida = energiaFornecida;
        this.valorTotal = calcularValor();
        this.statusSessao = StatusSessao.FINALIZADA;
        veiculo.receberCarga(energiaFornecida);
    }

    public double calcularValor() {
        return estrategia.calcularPreco(energiaConsumida);
    }

    public void cancelarSessao() {
        this.statusSessao = StatusSessao.CANCELADA;
        System.out.printf("  [SESSÃO] Sessão do veículo %s cancelada.%n", veiculo.getModelo());
    }

    public void exibirResumo() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("  ||======================================================");
        System.out.println("  || RESUMO DA SESSÃO");
        System.out.printf("  || Veículo   : %s%n", veiculo.getModelo());
        System.out.printf("  || Estação   : %s%n", estacao.getId());
        System.out.printf("  || Estratégia: %s%n", estrategia.getNome());
        System.out.printf("  || Início    : %s%n", tempoInicio.format(fmt));
        System.out.printf("  || Energia   : %.2f kWh%n", energiaConsumida);
        System.out.printf("  || Valor      : R$ %.2f%n", valorTotal);
        System.out.printf("  || Status    : %s%n", statusSessao);
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