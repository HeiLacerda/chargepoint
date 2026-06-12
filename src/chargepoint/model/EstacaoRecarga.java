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
    }

    public SessaoRecarga iniciarRecarga(VeiculoEletrico veiculo, EstrategiaRecarga estrategia) throws EstacaoOcupadaException, ConectorIncompativelException, EnergiaInsuficienteException {

        if (status == StatusEstacao.OCUPADA) {
            throw new EstacaoOcupadaException("Estação " + id + " ocupada.");
        }
        if (status == StatusEstacao.MANUTENCAO) {
            throw new EstacaoOcupadaException("Estação " + id + " em manutenção.");
        }
        if (veiculo.getTipoConector() != tipoConector) {
            throw new ConectorIncompativelException("Conector " + veiculo.getTipoConector() + " incompatível com " + tipoConector);
        }
        if (energiaDisponivel < veiculo.getEnergiaParaEncher()) {
            throw new EnergiaInsuficienteException("Energia insuficiente: " + String.format("%.1f", energiaDisponivel) + " kWh disponíveis.");
        }

        sessaoAtual = new SessaoRecarga(veiculo, this, estrategia);
        status = StatusEstacao.OCUPADA;

        System.out.printf("%n  ||== [%s] Iniciando recarga: %s ==||%n", id, veiculo.getModelo());
        System.out.printf("  ||  Bateria inicial: %.0f kWh / %.0f kWh (%.0f%%)%n",
                veiculo.getNivelBateria(), veiculo.getCapacidadeBateria(), veiculo.getPercentualBateria());
        System.out.printf("  ||  +1 kWh por segundo até completar%n");
        System.out.println("  ||======================================================");

        return sessaoAtual;
    }

    public void executarRecargaTempoReal(VeiculoEletrico veiculo, EstrategiaRecarga estrategia) throws EstacaoOcupadaException, ConectorIncompativelException, EnergiaInsuficienteException {

        SessaoRecarga sessao = iniciarRecarga(veiculo, estrategia);

        System.out.print("  ");
        while (sessao.tick()) {
            energiaDisponivel -= 1.0;
            try {
                Thread.sleep(1000); // 1 segundo real = 1 kWh
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        energiaDisponivel = Math.max(0, energiaDisponivel);

        historicoRecargas.add(sessaoAtual);

        if (deveEntrarManutencao()) {
            entrarManutencao();
        } else {
            status = StatusEstacao.DISPONIVEL;
            sessaoAtual = null;
        }

        proximoDaFila(estrategia);
    }

    public void adicionarFila(VeiculoEletrico veiculo) {
        filaDeEspera.offer(veiculo);
        imprimirFila();
    }

    public void proximoDaFila(EstrategiaRecarga estrategia) {
        if (filaDeEspera.isEmpty()) {
            System.out.printf("%n  [FILA %s] Fila vazia. Estação aguardando.%n", id);
            return;
        }
        VeiculoEletrico proximo = filaDeEspera.poll();
        System.out.printf("%n  [FILA %s] ➜ Próximo: %s. Iniciando recarga automática...%n", id, proximo.getModelo());
        imprimirFila();
        try {
            executarRecargaTempoReal(proximo, estrategia);
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    public void imprimirFila() {
        System.out.println("\n  ||=== FILA DE ESPERA — Estação " + id + " ===");
        if (filaDeEspera.isEmpty()) {
            System.out.println("  ||  (fila vazia)");
        } else {
            int pos = 1;
            for (VeiculoEletrico v : filaDeEspera) {
                System.out.printf("  ||  %d. %s [%s] — Bateria: %.0f%%%n", pos++, v.getModelo(), v.getPlaca(), v.getPercentualBateria());
            }
        }
        System.out.println("  ||======================================================");
    }

    public boolean deveEntrarManutencao() {
        return (energiaDisponivel / 500.0) * 100 <= LIMITE_MANUTENCAO;
    }

    public void entrarManutencao() {
        status = StatusEstacao.MANUTENCAO;
        System.out.printf("%n  [MANUTENÇÃO] Estação %s — energia baixa (%.1f kWh restantes).%n", id, energiaDisponivel);
    }

    public void recarregarEstacao(double energia) {
        energiaDisponivel += energia;
        if (status == StatusEstacao.MANUTENCAO || status == StatusEstacao.SEM_ENERGIA) {
            status = StatusEstacao.DISPONIVEL;
        }
        System.out.printf("  [ESTAÇÃO %s] +%.1f kWh. Total: %.1f kWh. Status: %s%n", id, energia, energiaDisponivel, status);
    }

    public void gerarRelatorio() {
        double totalEnergia = historicoRecargas.stream().mapToDouble(SessaoRecarga::getEnergiaConsumida).sum();
        double totalReceita = historicoRecargas.stream().mapToDouble(SessaoRecarga::getValorTotal).sum();
        System.out.println("\n  ||======================================================");
        System.out.printf("  || RELATÓRIO — Estação %s%n", id);
        System.out.println("  ||======================================================");
        System.out.printf("  || Localização   : %s%n", localizacao);
        System.out.printf("  || Energia disp. : %.1f kWh%n", energiaDisponivel);
        System.out.printf("  || Status        : %s%n", status);
        System.out.printf("  || Sessões       : %d%n", historicoRecargas.size());
        System.out.printf("  || Energia total : %.1f kWh%n", totalEnergia);
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