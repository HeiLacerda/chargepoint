package chargepoint.model;

public class RecargaRapida implements EstrategiaRecarga {

    private static final double PRECO_POR_KWH = 1.40;

    @Override
    public double calcularTempo(double energiaNecessaria, double potenciaEstacao) {
        double potenciaEfetiva = potenciaEstacao * 0.70;
        return energiaNecessaria / potenciaEfetiva;
    }

    @Override
    public double calcularPreco(double energiaConsumida) {
        return energiaConsumida * PRECO_POR_KWH;
    }

    @Override
    public String getNome() {
        return "Recarga Rápida (DC)";
    }
}