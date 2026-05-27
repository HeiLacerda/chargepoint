package database;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class BancoDeDadosFake {

    public static List<Cliente> clientes = new ArrayList<>();

    public static List<Administrador> administradores = new ArrayList<>();

    public static List<EstacaoRecarga> estacoes = new ArrayList<>();

    static {

        Cliente cliente1 = new Cliente("Heitor", "heitor@gmail.com", "123", 500);

        Cliente cliente2 = new Cliente("Maria", "maria@gmail.com", "456", 800);

        Cliente cliente3 = new Cliente("João", "joao@gmail.com", "789", 1200);

        Cliente cliente4 = new Cliente("Ana", "ana@gmail.com", "111", 300);

        VeiculoEletrico carro1 = new VeiculoEletrico("BYD Dolphin", "ABC-1234", 44);

        VeiculoEletrico carro2 = new VeiculoEletrico("Tesla Model 3", "TES-2025", 75);

        VeiculoEletrico carro3 = new VeiculoEletrico("Volvo EX30", "VOL-3030", 69);

        VeiculoEletrico carro4 = new VeiculoEletrico("Nissan Leaf", "LEA-2024", 40);

        cliente1.cadastrarVeiculo(carro1);
        cliente2.cadastrarVeiculo(carro2);
        cliente3.cadastrarVeiculo(carro3);
        cliente4.cadastrarVeiculo(carro4);

        clientes.add(cliente1);
        clientes.add(cliente2);
        clientes.add(cliente3);
        clientes.add(cliente4);

        Administrador admin1 = new Administrador("Carlos", "admin@gmail.com", "admin123");

        Administrador admin2 = new Administrador("Fernanda", "fernanda@admin.com", "admin456");

        administradores.add(admin1);
        administradores.add(admin2);

        estacoes.add(new EstacaoRecarga(1, "Shopping Cariri", 150));

        estacoes.add(new EstacaoRecarga(2, "Centro de Juazeiro", 100));

        estacoes.add(new EstacaoRecarga(3, "UFCA", 200));

        estacoes.add(new EstacaoRecarga(4, "North Shopping", 120));

        estacoes.add(new EstacaoRecarga(5, "Centro de Barbalha", 180));
    }
}