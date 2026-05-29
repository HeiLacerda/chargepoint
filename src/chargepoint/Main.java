package chargepoint;

import chargepoint.enums.TipoConector;
import chargepoint.exceptions.ConectorIncompativelException;
import chargepoint.exceptions.EnergiaInsuficienteException;
import chargepoint.exceptions.EstacaoOcupadaException;
import chargepoint.model.*;

public class Main {

    public static void main(String[] args) {

        System.out.println("======================================================");
        System.out.println("   CHARGEPOINT MANAGER — SIMULAÇÃO DO SISTEMA");
        System.out.println("======================================================");

        CentralRecarga central = new CentralRecarga("Central EletricHub Juazeiro");

        EstacaoRecarga estacao1 = new EstacaoRecarga("EST-001", "Shopping Norte", 50.0, 300.0, TipoConector.CCS2);
        EstacaoRecarga estacao2 = new EstacaoRecarga("EST-002", "Posto Central", 150.0, 450.0, TipoConector.CCS2);
        EstacaoRecarga estacao3 = new EstacaoRecarga("EST-003", "Aeroporto", 22.0, 200.0, TipoConector.TIPO2);
        EstacaoRecarga estacao4 = new EstacaoRecarga("EST-004", "Hospital", 75.0, 50.0, TipoConector.CHADEMO);

        central.adicionarEstacao(estacao1);
        central.adicionarEstacao(estacao2);
        central.adicionarEstacao(estacao3);
        central.adicionarEstacao(estacao4);

        VeiculoEletrico carro1 = new VeiculoEletrico("Tesla Model 3", "ABC-1234", 75.0, 20.0, TipoConector.CCS2);
        VeiculoEletrico carro2 = new VeiculoEletrico("BYD Atto 3", "XYZ-5678", 60.0, 10.0, TipoConector.CCS2);
        VeiculoEletrico carro3 = new VeiculoEletrico("Nissan Leaf", "DEF-9999", 40.0, 15.0, TipoConector.CHADEMO);
        VeiculoEletrico carro4 = new VeiculoEletrico("Renault Zoe", "GHI-3333", 52.0, 5.0, TipoConector.TIPO2);

        EstrategiaRecarga lenta = new RecargaLenta();
        EstrategiaRecarga rapida = new RecargaRapida();
        EstrategiaRecarga ultraRapida = new RecargaUltraRapida();

        // SIMULAÇÃO 1 — Monitoramento inicial
        System.out.println("\n>>> [SIM 1] Monitoramento inicial das estações:");
        central.monitorarEstacoes();

        // SIMULAÇÃO 2 — Recarga direta com estratégia ultra rápida
        System.out.println("\n>>> [SIM 2] Recarga direta — Tesla Model 3 na EST-002:");
        try {
            estacao2.iniciarRecarga(carro1, ultraRapida);
            estacao2.encerrarRecarga(carro1.getEnergiaParaEncher());
        } catch (EstacaoOcupadaException | ConectorIncompativelException | EnergiaInsuficienteException e) {
            System.out.println("ERRO: " + e.getMessage());
        }

        // SIMULAÇÃO 3 — Central decide a melhor estação
        System.out.println("\n>>> [SIM 3] Central buscando melhor estação para BYD Atto 3:");
        central.conectarVeiculoInteligente(carro2, rapida);

        // Encerrar a sessão do carro2 na estação que foi alocado
        // (estacao2, pois é a de maior potência disponível com CCS2)
        try {
            estacao2.encerrarRecarga(carro2.getEnergiaParaEncher());
        } catch (Exception e) {
            System.out.println("INFO: " + e.getMessage());
        }

        // SIMULAÇÃO 4 — Conector Incompatível
        System.out.println("\n>>> [SIM 4] Tentando conectar Nissan Leaf (CHAdeMO) na EST-001 (CCS2):");
        try {
            estacao1.iniciarRecarga(carro3, rapida);
        } catch (ConectorIncompativelException e) {
            System.out.println("  [EXCEÇÃO] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }

        // SIMULAÇÃO 5 — Fila de espera automática
        System.out.println("\n>>> [SIM 5] Testando fila de espera na EST-001:");
        VeiculoEletrico extra1 = new VeiculoEletrico("Chevrolet Bolt", "JKL-0001", 65.0, 10.0, TipoConector.CCS2);
        VeiculoEletrico extra2 = new VeiculoEletrico("Volvo XC40", "MNO-0002", 78.0, 8.0, TipoConector.CCS2);

        try {
            estacao1.iniciarRecarga(extra1, rapida); // ocupa a estação
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }

        // Tenta colocar carro2 enquanto estação está ocupada
        try {
            estacao1.iniciarRecarga(extra2, rapida);
        } catch (EstacaoOcupadaException e) {
            System.out.println("  [EXCEÇÃO] " + e.getMessage());
            estacao1.adicionarFila(extra2); // coloca na fila
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }

        // Encerrar recarga do extra1 → ativa próximo da fila automaticamente
        estacao1.encerrarRecarga(extra1.getEnergiaParaEncher());

        // SIMULAÇÃO 6 — Energia insuficiente
        System.out.println("\n>>> [SIM 6] Energia insuficiente — EST-004:");
        VeiculoEletrico grandeCarro = new VeiculoEletrico("BMW iX", "PQR-9876", 100.0, 0.0, TipoConector.CHADEMO);
        try {
            estacao4.iniciarRecarga(grandeCarro, ultraRapida);
        } catch (EnergiaInsuficienteException e) {
            System.out.println("  [EXCEÇÃO] " + e.getMessage());
            System.out.println("  [AÇÃO] Recarregando estação 4...");
            estacao4.recarregarEstacao(300.0);

            // Tenta novamente após recarga
            try {
                estacao4.iniciarRecarga(grandeCarro, rapida);
                estacao4.encerrarRecarga(grandeCarro.getEnergiaParaEncher());
            } catch (Exception ex) {
                System.out.println("  [ERRO] " + ex.getMessage());
            }
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }

        // SIMULAÇÃO 7 — Verificar sobrecarga
        System.out.println("\n>>> [SIM 7] Verificação de sobrecarga do sistema:");
        central.verificarSobrecarga();

        // SIMULAÇÃO 8 — Monitoramento final + Relatório geral
        System.out.println("\n>>> [SIM 8] Monitoramento final:");
        central.monitorarEstacoes();

        System.out.println("\n>>> [SIM 9] Relatório geral da central:");
        central.gerarRelatorioGeral();

        System.out.println("\n======================================================");
        System.out.println("   SIMULAÇÃO ENCERRADA");
        System.out.println("======================================================");
    }
}