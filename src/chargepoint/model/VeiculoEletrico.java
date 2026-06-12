package chargepoint.model;

import chargepoint.enums.TipoConector;

public class VeiculoEletrico {

    private String modelo;
    private String placa;
    private double nivelBateria;
    private double capacidadeBateria;
    private TipoConector tipoConector;

    public VeiculoEletrico(String modelo, String placa, double capacidadeBateria, double nivelBateria, TipoConector tipoConector) {
        this.modelo = modelo;
        this.placa = placa;
        this.capacidadeBateria = capacidadeBateria;
        this.nivelBateria = nivelBateria;
        this.tipoConector = tipoConector;
    }

    // Adiciona exatamente 1 kWh (chamado a cada segundo)
    public boolean receberUmKwh() {
        if (nivelBateria < capacidadeBateria) {
            nivelBateria += 1.0;
            if (nivelBateria > capacidadeBateria) nivelBateria = capacidadeBateria;
            return true;
        }
        return false;
    }

    public double getPercentualBateria() {
        return (nivelBateria / capacidadeBateria) * 100.0;
    }

    public double getEnergiaParaEncher() {
        return capacidadeBateria - nivelBateria;
    }

    public boolean bateriaCheinha() {
        return nivelBateria >= capacidadeBateria;
    }

    public String getModelo() {
        return modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public double getNivelBateria() {
        return nivelBateria;
    }

    public double getCapacidadeBateria() {
        return capacidadeBateria;
    }

    public TipoConector getTipoConector() {
        return tipoConector;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] | Conector: %s | Bateria: %.0f/%.0f kWh (%.0f%%)",
                modelo, placa, tipoConector,
                nivelBateria, capacidadeBateria, getPercentualBateria());
    }
}