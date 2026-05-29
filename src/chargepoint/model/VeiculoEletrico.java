package chargepoint.model;

import chargepoint.enums.TipoConector;

public class VeiculoEletrico {

    private String modelo;
    private String placa;
    private double capacidadeBateria; // kWh
    private double nivelBateria;      // kWh atual
    private TipoConector tipoConector;

    public VeiculoEletrico(String modelo, String placa, double capacidadeBateria, double nivelBateria, TipoConector tipoConector) {
        this.modelo = modelo;
        this.placa = placa;
        this.capacidadeBateria = capacidadeBateria;
        this.nivelBateria = nivelBateria;
        this.tipoConector = tipoConector;
    }

    public void receberCarga(double energiaRecebida) {
        double novoNivel = this.nivelBateria + energiaRecebida;
        if (novoNivel > capacidadeBateria) {
            this.nivelBateria = capacidadeBateria;
        } else {
            this.nivelBateria = novoNivel;
        }
        System.out.printf("  [VEÍCULO] %s recebeu %.1f kWh. Bateria: %.1f/%.1f kWh (%.0f%%)%n", modelo, energiaRecebida, nivelBateria, capacidadeBateria, getPercentualBateria());
    }

    public double verificarBateria() {
        return getPercentualBateria();
    }

    public double getPercentualBateria() {
        return (nivelBateria / capacidadeBateria) * 100;
    }

    public double getEnergiaParaEncher() {
        return capacidadeBateria - nivelBateria;
    }

    // Getters
    public String getModelo() {
        return modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public double getCapacidadeBateria() {
        return capacidadeBateria;
    }

    public double getNivelBateria() {
        return nivelBateria;
    }

    public TipoConector getTipoConector() {
        return tipoConector;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] | Conector: %s | Bateria: %.0f%%", modelo, placa, tipoConector, getPercentualBateria());
    }
}