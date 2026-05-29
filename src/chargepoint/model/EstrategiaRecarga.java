package chargepoint.model;

public interface EstrategiaRecarga {
    double calcularTempo(double energiaNecessaria, double potenciaEstacao);

    double calcularPreco(double energiaConsumida);

    String getNome();
}