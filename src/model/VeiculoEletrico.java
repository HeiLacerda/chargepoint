package model;

public class VeiculoEletrico {

    private String modelo;
    private String placa;
    private double capacidadeBateria;

    public VeiculoEletrico(String modelo, String placa, double capacidadeBateria) {
        this.modelo = modelo;
        this.placa = placa;
        this.capacidadeBateria = capacidadeBateria;
    }

    public String getModelo() {
        return modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public double getCapacidadeBateria() {
        return capacidadeBateria;
    }

    @Override
    public String toString() {
        return "Modelo: " + modelo + " | Placa: " + placa + " | Capacidade: " + capacidadeBateria + "kWh";
    }
}