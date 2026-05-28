package database;

import enums.TipoConector;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class BancoDeDadosFake {

    public static List<Cliente> clientes = new ArrayList<>();

    public static List<Administrador> administradores = new ArrayList<>();

    public static List<EstacaoRecarga> estacoes = new ArrayList<>();

    static {

        Cliente cliente1 = new Cliente("Heitor", "heitor@gmail.com", "123", 500);

        Cliente cliente2 = new Cliente("Maria", "maria@gmail.com", "456", 1200);

        Cliente cliente3 = new Cliente("João", "joao@gmail.com", "789", 300);

        Cliente cliente4 = new Cliente("Ana", "ana@gmail.com", "111", 900);

        Cliente cliente5 = new Cliente("Lucas", "lucas@gmail.com", "222", 150);

        Cliente cliente6 = new Cliente("Fernanda", "fernanda@gmail.com", "333", 2000);

        VeiculoEletrico carro1 = new VeiculoEletrico("BYD Dolphin", "ABC-1234", 44, TipoConector.CCS2);

        VeiculoEletrico carro2 = new VeiculoEletrico("Tesla Model 3", "TES-2025", 75, TipoConector.TIPO2);

        VeiculoEletrico carro3 = new VeiculoEletrico("Nissan Leaf", "LEA-2024", 40, TipoConector.CHADEMO);

        VeiculoEletrico carro4 = new VeiculoEletrico("Volvo EX30", "VOL-3030", 69, TipoConector.CCS2);

        VeiculoEletrico carro5 = new VeiculoEletrico("Renault Zoe", "ZOE-9090", 52, TipoConector.TIPO2);

        VeiculoEletrico carro6 = new VeiculoEletrico("Kwid E-Tech", "KWD-1111", 27, TipoConector.CCS2);

        carro1.carregar(8);

        carro2.carregar(20);

        carro3.carregar(5);

        carro4.carregar(40);

        carro5.carregar(10);

        carro6.carregar(2);

        carro2.dirigir(50);

        carro4.dirigir(80);

        cliente1.cadastrarVeiculo(carro1);

        cliente2.cadastrarVeiculo(carro2);

        cliente3.cadastrarVeiculo(carro3);

        cliente4.cadastrarVeiculo(carro4);

        cliente5.cadastrarVeiculo(carro5);

        cliente6.cadastrarVeiculo(carro6);

        clientes.add(cliente1);

        clientes.add(cliente2);

        clientes.add(cliente3);

        clientes.add(cliente4);

        clientes.add(cliente5);

        clientes.add(cliente6);

        Administrador admin1 = new Administrador("Carlos", "admin@gmail.com", "admin123");

        Administrador admin2 = new Administrador("Juliana", "juliana@admin.com", "admin456");

        administradores.add(admin1);

        administradores.add(admin2);

        EstacaoRecarga estacao1 = new EstacaoRecarga(1, "Shopping Cariri", 150, TipoConector.CCS2);

        EstacaoRecarga estacao2 = new EstacaoRecarga(2, "Centro de Juazeiro", 120, TipoConector.TIPO2);

        EstacaoRecarga estacao3 = new EstacaoRecarga(3, "UFCA", 200, TipoConector.CHADEMO);

        EstacaoRecarga estacao4 = new EstacaoRecarga(4, "North Shopping", 180, TipoConector.CCS2);

        EstacaoRecarga estacao5 = new EstacaoRecarga(5, "Centro de Barbalha", 100, TipoConector.TIPO2);

        estacao1.fornecerEnergia(120);

        estacao2.fornecerEnergia(300);

        estacao3.fornecerEnergia(450);

        estacao4.fornecerEnergia(50);

        estacao5.fornecerEnergia(200);

        estacoes.add(estacao1);

        estacoes.add(estacao2);

        estacoes.add(estacao3);

        estacoes.add(estacao4);

        estacoes.add(estacao5);
    }
}