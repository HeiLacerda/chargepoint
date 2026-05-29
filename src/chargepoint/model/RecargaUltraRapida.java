package chargepoint.model;

public class RecargaUltraRapida implements EstrategiaRecarga {

    private static final double PRECO_POR_KWH = 2.20;

    @Override
    public double calcularTempo(double energiaNecessaria, double potenciaEstacao) {
        return energiaNecessaria / potenciaEstacao;
    }

    @Override
    public double calcularPreco(double energiaConsumida) {
        return energiaConsumida * PRECO_POR_KWH;
    }

    @Override
    public String getNome() {
        return "Recarga Ultra Rápida (DC)";
    }
}