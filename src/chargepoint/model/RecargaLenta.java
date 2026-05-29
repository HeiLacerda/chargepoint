package chargepoint.model;

public class RecargaLenta implements EstrategiaRecarga {

    private static final double PRECO_POR_KWH = 0.80;

    @Override
    public double calcularTempo(double energiaNecessaria, double potenciaEstacao) {
        double potenciaEfetiva = potenciaEstacao * 0.30;
        return energiaNecessaria / potenciaEfetiva;
    }

    @Override
    public double calcularPreco(double energiaConsumida) {
        return energiaConsumida * PRECO_POR_KWH;
    }

    @Override
    public String getNome() {
        return "Recarga Lenta (AC)";
    }
}