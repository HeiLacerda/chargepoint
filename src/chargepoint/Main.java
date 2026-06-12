package chargepoint;

import chargepoint.enums.TipoConector;
import chargepoint.exceptions.ConectorIncompativelException;
import chargepoint.exceptions.EnergiaInsuficienteException;
import chargepoint.exceptions.EstacaoOcupadaException;
import chargepoint.model.*;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        System.out.println("||======================================================");
        System.out.println("||     CHARGEPOINT MANAGER — SIMULACAO COMPLETA    ");
        System.out.println("||======================================================");

        // SETUP: Central + Estacoes + Veiculos
        CentralRecarga central = new CentralRecarga("Central EletricHub Juazeiro");

        EstacaoRecarga estacao1 = new EstacaoRecarga("EST-001", "Shopping Norte", 50.0, 500.0, TipoConector.CCS2);
        EstacaoRecarga estacao2 = new EstacaoRecarga("EST-002", "Posto Central", 150.0, 450.0, TipoConector.CCS2);
        EstacaoRecarga estacao3 = new EstacaoRecarga("EST-003", "Aeroporto", 22.0, 200.0, TipoConector.TIPO2);
        EstacaoRecarga estacao4 = new EstacaoRecarga("EST-004", "Hospital", 75.0, 50.0, TipoConector.CHADEMO);

        central.adicionarEstacao(estacao1);
        central.adicionarEstacao(estacao2);
        central.adicionarEstacao(estacao3);
        central.adicionarEstacao(estacao4);

        // Veiculos para testes estaticos (excecoes, relatorios)
        VeiculoEletrico carroA = new VeiculoEletrico("Tesla Model S", "AAA-0001", 100.0, 60.0, TipoConector.CCS2);
        VeiculoEletrico carroB = new VeiculoEletrico("Nissan Leaf", "BBB-0002", 40.0, 15.0, TipoConector.CHADEMO);
        VeiculoEletrico carroC = new VeiculoEletrico("BMW i3", "CCC-0003", 80.0, 20.0, TipoConector.CCS2);
        VeiculoEletrico carroD = new VeiculoEletrico("Renault Zoe", "DDD-0004", 52.0, 5.0, TipoConector.TIPO2);
        VeiculoEletrico carroE = new VeiculoEletrico("BMW iX", "EEE-0005", 100.0, 0.0, TipoConector.CHADEMO);

        // Veiculos para simulacao em tempo real (bateria pequena = demo rapida)
        // capacidade baixa para nao demorar: faltam poucos kWh
        VeiculoEletrico carro1 = new VeiculoEletrico("Tesla Model 3", "ABC-1234", 10.0, 5.0, TipoConector.CCS2);
        VeiculoEletrico carro2 = new VeiculoEletrico("BYD Atto 3", "XYZ-5678", 8.0, 4.0, TipoConector.CCS2);
        VeiculoEletrico carro3 = new VeiculoEletrico("Chevrolet Bolt", "DEF-9999", 7.0, 4.0, TipoConector.CCS2);

        EstrategiaRecarga lenta = new RecargaLenta();
        EstrategiaRecarga rapida = new RecargaRapida();
        EstrategiaRecarga ultraRapida = new RecargaUltraRapida();

        // [SIM 1] Monitoramento inicial
        System.out.println("\n>>> [SIM 1] Monitoramento inicial das estacoes:");
        central.monitorarEstacoes();

        // [SIM 2] Conector incompativel (excecao)
        System.out.println("\n>>> [SIM 2] Tentando conectar Nissan Leaf (CHAdeMO) na EST-001 (CCS2):");
        try {
            estacao1.iniciarRecarga(carroB, rapida);
        } catch (ConectorIncompativelException e) {
            System.out.println("  [EXCECAO CAPTURADA] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }

        // [SIM 3] Estacao ocupada (excecao)
        System.out.println("\n>>> [SIM 3] Tentando usar EST-003 que ja esta ocupada:");
        try {
            // Coloca carroD na estacao 3 (TIPO2) para ocupar ela
            estacao3.iniciarRecarga(carroD, rapida);

            // Tenta colocar um segundo carro TIPO2 enquanto ela esta ocupada
            VeiculoEletrico carroExtra = new VeiculoEletrico("Peugeot e-208", "ZZZ-9999", 50.0, 10.0, TipoConector.TIPO2);
            estacao3.iniciarRecarga(carroExtra, rapida);

        } catch (EstacaoOcupadaException e) {
            System.out.println("  [EXCECAO CAPTURADA] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }

        // [SIM 4] Central buscando melhor estacao
        System.out.println("\n>>> [SIM 4] Central buscando melhor estacao para Tesla Model S:");
        Optional<EstacaoRecarga> melhor = central.buscarMelhorEstacao(carroA);
        if (melhor.isPresent()) {
            System.out.printf("  [CENTRAL] Melhor estacao encontrada: %s (%.0f kW, %.0f kWh disp.)%n", melhor.get().getId(), melhor.get().getPotencia(), melhor.get().getEnergiaDisponivel());
        }

        // [SIM 5] Verificar sobrecarga
        System.out.println("\n>>> [SIM 5] Verificando sobrecarga do sistema:");
        central.verificarSobrecarga();

        // [SIM 6] RECARGA EM TEMPO REAL + FILA AO VIVO
        // carro2 e carro3 entram na fila,
        // carro1 comeca a recarregar ao vivo (+1 kWh/segundo),
        // quando termina o proximo entra automaticamente
        System.out.println("\n>>> [SIM 6] Simulacao ao vivo com fila de espera:");
        System.out.println("    carro2 e carro3 chegam e entram na fila enquanto carro1 carrega...");

        estacao1.adicionarFila(carro2);
        estacao1.adicionarFila(carro3);

        try {
            estacao1.executarRecargaTempoReal(carro1, rapida);
        } catch (Exception e) {
            System.out.println("[ERRO] " + e.getMessage());
        }

        // [SIM 7] Monitoramento final + relatorio geral
        System.out.println("\n>>> [SIM 7] Monitoramento final:");
        central.monitorarEstacoes();

        System.out.println("\n>>> [SIM 8] Relatorio geral da central:");
        central.gerarRelatorioGeral();

        System.out.println("\n||======================================================");
        System.out.println("||              SIMULACAO ENCERRADA                ");
        System.out.println("||======================================================");
    }
}