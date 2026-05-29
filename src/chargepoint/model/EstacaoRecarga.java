package chargepoint.model;

import chargepoint.enums.StatusEstacao;
import chargepoint.enums.TipoConector;
import chargepoint.exceptions.ConectorIncompativelException;
import chargepoint.exceptions.EnergiaInsuficienteException;
import chargepoint.exceptions.EstacaoOcupadaException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class EstacaoRecarga {

    private String id;
    private String localizacao;
    private double potencia;
    private double energiaDisponivel;
    private StatusEstacao status;
    private TipoConector tipoConector;
    private Queue<VeiculoEletrico> filaDeEspera;
    private List<SessaoRecarga> historicoRecargas;
    private SessaoRecarga sessaoAtual;

    private static final double LIMITE_MANUTENCAO = 10.0;

    public EstacaoRecarga(String id, String localizacao, double potencia, double energiaDisponivel, TipoConector tipoConector) {
        this.id = id;
        this.localizacao = localizacao;
        this.potencia = potencia;
        this.energiaDisponivel = energiaDisponivel;
        this.tipoConector = tipoConector;
        this.status = StatusEstacao.DISPONIVEL;
        this.filaDeEspera = new LinkedList<>();
        this.historicoRecargas = new ArrayList<>();
        this.sessaoAtual = null;
    }

    public SessaoRecarga iniciarRecarga(VeiculoEletrico veiculo, EstrategiaRecarga estrategia) throws EstacaoOcupadaException, ConectorIncompativelException, EnergiaInsuficienteException {

        if (status == StatusEstacao.OCUPADA) {
            throw new EstacaoOcupadaException("Estação " + id + " está ocupada. Veículo adicionado à fila.");
        }
        if (status == StatusEstacao.MANUTENCAO) {
            throw new EstacaoOcupadaException("Estação " + id + " está em manutenção e não pode ser utilizada.");
        }
        if (!verificarConector(veiculo)) {
            throw new ConectorIncompativelException("Conector do veículo (" + veiculo.getTipoConector() + ") é incompatível com a estação (" + tipoConector + ").");
        }
        if (!verificarEnergia(veiculo.getEnergiaParaEncher())) {
            throw new EnergiaInsuficienteException("Energia insuficiente na estação " + id + ". Disponível: " + String.format("%.1f", energiaDisponivel) + " kWh.");
        }

        sessaoAtual = new SessaoRecarga(veiculo, this, estrategia);
        status = StatusEstacao.OCUPADA;

        System.out.printf("%n  [ESTAÇÃO %s] Recarga INICIADA para %s usando %s.%n", id, veiculo.getModelo(), estrategia.getNome());

        double tempoEstimado = estrategia.calcularTempo(veiculo.getEnergiaParaEncher(), potencia);
        System.out.printf("  [ESTAÇÃO %s] Tempo estimado: %.2f hora(s).%n", id, tempoEstimado);

        return sessaoAtual;
    }

    public void encerrarRecarga(double energiaFornecida) {
        if (sessaoAtual == null || sessaoAtual.getStatusSessao().name().equals("FINALIZADA")) {
            System.out.println("  [ESTAÇÃO " + id + "] Nenhuma sessão ativa para encerrar.");
            return;
        }

        sessaoAtual.finalizarSessao(energiaFornecida);
        energiaDisponivel -= energiaFornecida;
        historicoRecargas.add(sessaoAtual);

        System.out.printf("  [ESTAÇÃO %s] Recarga ENCERRADA. Energia restante: %.1f kWh.%n", id, energiaDisponivel);

        sessaoAtual.exibirResumo();

        if (deveEntrarManutencao()) {
            entrarManutencao();
        } else {
            status = StatusEstacao.DISPONIVEL;
            sessaoAtual = null;
            proximoDaFila();
        }
    }

    public void adicionarFila(VeiculoEletrico veiculo) {
        filaDeEspera.offer(veiculo);
        System.out.printf("  [FILA %s] %s adicionado à fila. Posição: %d.%n", id, veiculo.getModelo(), filaDeEspera.size());
    }

    public void proximoDaFila() {
        if (filaDeEspera.isEmpty()) {
            System.out.printf("  [FILA %s] Fila vazia. Estação aguardando.%n", id);
            return;
        }
        VeiculoEletrico proximo = filaDeEspera.poll();
        System.out.printf("%n  [FILA %s] Próximo veículo: %s. Iniciando recarga automática...%n", id, proximo.getModelo());
        try {
            iniciarRecarga(proximo, new RecargaRapida());
        } catch (Exception e) {
            System.out.println("  [FILA] Erro ao iniciar recarga automática: " + e.getMessage());
        }
    }

    public boolean verificarEnergia(double energiaNecessaria) {
        return energiaDisponivel >= energiaNecessaria;
    }

    public boolean deveEntrarManutencao() {
        double percentualRestante = (energiaDisponivel / 500.0) * 100;
        return percentualRestante <= LIMITE_MANUTENCAO;
    }

    public void entrarManutencao() {
        this.status = StatusEstacao.MANUTENCAO;
        System.out.printf("  [MANUTENÇÃO] Estação %s entrou em MANUTENÇÃO. Energia: %.1f kWh.%n", id, energiaDisponivel);
    }

    public void recarregarEstacao(double energiaAdicionada) {
        this.energiaDisponivel += energiaAdicionada;
        System.out.printf("  [ESTAÇÃO %s] Energia recarregada: +%.1f kWh. Total: %.1f kWh.%n", id, energiaAdicionada, energiaDisponivel);

        if (status == StatusEstacao.MANUTENCAO || status == StatusEstacao.SEM_ENERGIA) {
            this.status = StatusEstacao.DISPONIVEL;
            System.out.printf("  [ESTAÇÃO %s] Status atualizado para DISPONÍVEL.%n", id);
        }
    }

    public boolean verificarConector(VeiculoEletrico veiculo) {
        return this.tipoConector == veiculo.getTipoConector();
    }

    public void gerarRelatorio() {
        System.out.println("\n  ||======================================================");
        System.out.printf("  || RELATÓRIO — Estação %s%n", id);
        System.out.println("  ||======================================================");
        System.out.printf("  || Localização   : %s%n", localizacao);
        System.out.printf("  || Potência      : %.1f kW%n", potencia);
        System.out.printf("  || Energia disp. : %.1f kWh%n", energiaDisponivel);
        System.out.printf("  || Conector      : %s%n", tipoConector);
        System.out.printf("  || Status        : %s%n", status);
        System.out.printf("  || Fila de espera: %d veículo(s)%n", filaDeEspera.size());
        System.out.printf("  || Sessões total : %d%n", historicoRecargas.size());

        double totalEnergia = historicoRecargas.stream().mapToDouble(SessaoRecarga::getEnergiaConsumida).sum();
        double totalReceita = historicoRecargas.stream().mapToDouble(SessaoRecarga::getValorTotal).sum();

        System.out.printf("  || Energia total : %.2f kWh%n", totalEnergia);
        System.out.printf("  || Receita total : R$ %.2f%n", totalReceita);
        System.out.println("  ||======================================================");
    }

    public String getId() {
        return id;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public double getPotencia() {
        return potencia;
    }

    public double getEnergiaDisponivel() {
        return energiaDisponivel;
    }

    public StatusEstacao getStatus() {
        return status;
    }

    public TipoConector getTipoConector() {
        return tipoConector;
    }

    public int getTamanhoFila() {
        return filaDeEspera.size();
    }

    public List<SessaoRecarga> getHistorico() {
        return historicoRecargas;
    }
}