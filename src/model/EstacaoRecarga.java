package model;

import enums.StatusEstacao;
import enums.TipoConector;

import java.util.ArrayList;
import java.util.List;

public class EstacaoRecarga {

    private int id;

    private String localizacao;

    private double potencia;

    private double energiaDisponivel;

    private double capacidadeMaxima;

    private TipoConector conector;

    private StatusEstacao status;

    private List<String> historicoVeiculos = new ArrayList<>();

    public EstacaoRecarga(int id, String localizacao, double potencia, TipoConector conector) {

        this.id = id;

        this.localizacao = localizacao;

        this.potencia = potencia;

        this.conector = conector;

        this.capacidadeMaxima = 500;

        this.energiaDisponivel = 500;

        this.status = StatusEstacao.DISPONIVEL;
    }

    public EstacaoRecarga(int id, String localizacao, double potencia, TipoConector conector, double energiaInicial) {

        this.id = id;

        this.localizacao = localizacao;

        this.potencia = potencia;

        this.conector = conector;

        this.capacidadeMaxima = 500;

        this.energiaDisponivel = energiaInicial;

        this.status = StatusEstacao.DISPONIVEL;
    }

    public boolean podeCarregar(VeiculoEletrico veiculo, double kwh) {

        if (energiaDisponivel <= 0) {

            System.out.println("Estação sem energia.");

            return false;
        }

        if (veiculo.getConector() != conector) {

            System.out.println("Conector incompatível.");

            return false;
        }

        if (kwh > energiaDisponivel) {

            System.out.println("Energia insuficiente na estação.");

            return false;
        }

        return true;
    }

    public void fornecerEnergia(double kwh) {

        energiaDisponivel -= kwh;

        if (energiaDisponivel < 0) {

            energiaDisponivel = 0;
        }
    }

    public void recarregarEstacao(double energia) {

        energiaDisponivel += energia;

        if (energiaDisponivel > capacidadeMaxima) {

            energiaDisponivel = capacidadeMaxima;
        }

        System.out.println("Estação recarregada.");
    }

    public void adicionarHistorico(VeiculoEletrico veiculo) {

        historicoVeiculos.add(veiculo.getModelo() + " - " + veiculo.getPlaca());
    }

    public List<String> getHistoricoVeiculos() {

        return historicoVeiculos;
    }

    public void ocuparEstacao() {

        status = StatusEstacao.OCUPADA;
    }

    public void liberarEstacao() {

        status = StatusEstacao.DISPONIVEL;
    }

    public int getId() {
        return id;
    }

    public StatusEstacao getStatus() {
        return status;
    }

    public double getEnergiaDisponivel() {
        return energiaDisponivel;
    }

    @Override
    public String toString() {

        return "Estação " + id + " | " + localizacao + " | Energia: " + energiaDisponivel + "/" + capacidadeMaxima + " kWh" + " | Conector: " + conector + " | Status: " + status;
    }
}