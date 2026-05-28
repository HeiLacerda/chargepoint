package model;

import enums.TipoConector;

public class VeiculoEletrico {

    private String modelo;

    private String placa;

    private double capacidadeBateria;

    private double bateriaAtual;

    private TipoConector conector;

    public VeiculoEletrico(String modelo, String placa, double capacidadeBateria, TipoConector conector) {

        this.modelo = modelo;

        this.placa = placa;

        this.capacidadeBateria = capacidadeBateria;

        this.conector = conector;

        this.bateriaAtual = capacidadeBateria * 0.25;
    }

    public void carregar(double kwh) {

        if (bateriaAtual >= capacidadeBateria) {

            System.out.println("Bateria já está totalmente carregada.");

            return;
        }

        double espacoDisponivel = capacidadeBateria - bateriaAtual;

        if (kwh > espacoDisponivel) {

            kwh = espacoDisponivel;
        }

        bateriaAtual += kwh;

        System.out.println("Veículo carregado em " + kwh + " kWh.");
    }

    public void dirigir(double km) {

        double consumo = km * 0.2;

        bateriaAtual -= consumo;

        if (bateriaAtual < 0) {

            bateriaAtual = 0;
        }

        System.out.println("Veículo percorreu " + km + " km.");

        System.out.println("Consumo: " + consumo + " kWh");
    }

    public double getPorcentagemBateria() {

        return (bateriaAtual / capacidadeBateria) * 100;
    }

    public double getBateriaAtual() {
        return bateriaAtual;
    }

    public double getCapacidadeBateria() {
        return capacidadeBateria;
    }

    public TipoConector getConector() {
        return conector;
    }

    public String getModelo() {
        return modelo;
    }

    public String getPlaca() {
        return placa;
    }

    @Override
    public String toString() {

        return modelo + " | " + placa + " | " + bateriaAtual + "/" + capacidadeBateria + " kWh" + " (" + String.format("%.1f", getPorcentagemBateria()) + "%)" + " | Conector: " + conector;
    }
}