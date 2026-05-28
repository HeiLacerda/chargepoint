package app;

import database.BancoDeDadosFake;

import enums.TipoPagamento;

import enums.TipoConector;

import model.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int opcao;

        do {

            System.out.println("\n==============================");
            System.out.println(" CHARGEPOINT MANAGER");
            System.out.println("==============================");

            System.out.println("1 - Listar clientes");
            System.out.println("2 - Listar estações");
            System.out.println("3 - Realizar recarga");
            System.out.println("4 - Adicionar saldo");
            System.out.println("5 - Relatórios");
            System.out.println("6 - Cadastrar cliente");
            System.out.println("7 - Cadastrar veículo");
            System.out.println("8 - Cadastrar estação");
            System.out.println("9 - Dirigir veículo");
            System.out.println("10 - Recarregar estação");
            System.out.println("11 - Histórico de recargas");
            System.out.println("12 - Histórico da estação");
            System.out.println("0 - Sair");

            System.out.print("\nEscolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {

                case 1:

                    System.out.println("\n=== CLIENTES ===");

                    for (Cliente c : BancoDeDadosFake.clientes) {

                        System.out.println("Nome: " + c.getNome());

                        System.out.println("Email: " + c.getEmail());

                        System.out.println("Saldo: R$ " + c.getSaldo());

                        System.out.println("----------------");
                    }

                    break;

                case 2:

                    System.out.println("\n=== ESTAÇÕES ===");

                    for (EstacaoRecarga e : BancoDeDadosFake.estacoes) {

                        System.out.println(e);
                    }

                    break;

                case 3:

                    System.out.print("\nDigite o email do cliente: ");

                    String email = scanner.nextLine();

                    Cliente clienteSelecionado = null;

                    for (Cliente c : BancoDeDadosFake.clientes) {

                        if (c.getEmail().equals(email)) {

                            clienteSelecionado = c;
                            break;
                        }
                    }

                    if (clienteSelecionado == null) {

                        System.out.println("Cliente não encontrado.");

                        break;
                    }

                    System.out.println("\nCliente autenticado: " + clienteSelecionado.getNome());

                    System.out.println("\nVEÍCULOS:");

                    for (VeiculoEletrico v : clienteSelecionado.getVeiculos()) {

                        System.out.println(v);
                    }

                    System.out.println("\nVEÍCULOS:");

                    for (int i = 0; i < clienteSelecionado.getVeiculos().size(); i++) {

                        System.out.println((i + 1) + " - " + clienteSelecionado.getVeiculos().get(i));
                    }

                    System.out.print("\nEscolha o veículo: ");

                    int escolhaVeiculo = scanner.nextInt();

                    scanner.nextLine();

                    VeiculoEletrico veiculo = clienteSelecionado.getVeiculos().get(escolhaVeiculo - 1);

                    System.out.println("\nESTAÇÕES:");

                    for (EstacaoRecarga e : BancoDeDadosFake.estacoes) {

                        System.out.println(e);
                    }

                    System.out.print("\nDigite o ID da estação: ");

                    int id = scanner.nextInt();

                    System.out.print("Digite os kWh: ");

                    double kwh = scanner.nextDouble();

                    scanner.nextLine();

                    EstacaoRecarga estacao = null;

                    for (EstacaoRecarga e : BancoDeDadosFake.estacoes) {

                        if (e.getId() == id) {

                            estacao = e;
                            break;
                        }
                    }

                    if (estacao == null) {

                        System.out.println("Estação não encontrada.");

                        break;
                    }

                    try {

                        SessaoRecarga sessao = new SessaoRecarga(clienteSelecionado, veiculo, estacao);

                        boolean sucesso = sessao.iniciarRecarga(kwh);

                        if (!sucesso) {

                            break;
                        }

                        System.out.println("\n1 - PIX");

                        System.out.println("2 - CARTÃO");

                        System.out.print("Forma de pagamento: ");

                        int pagamentoEscolhido = scanner.nextInt();

                        scanner.nextLine();

                        TipoPagamento tipo;

                        if (pagamentoEscolhido == 1) {

                            tipo = TipoPagamento.PIX;

                        } else {

                            tipo = TipoPagamento.CARTAO;
                        }

                        Pagamento pagamento = new Pagamento(tipo);

                        pagamento.realizarPagamento(sessao.getValorTotal());

                        clienteSelecionado.descontarSaldo(sessao.getValorTotal());

                        sessao.encerrarRecarga();

                    } catch (Exception e) {

                        System.out.println("ERRO: " + e.getMessage());
                    }

                    break;

                case 4:

                    System.out.print("\nDigite o email do cliente: ");

                    String emailSaldo = scanner.nextLine();

                    Cliente clienteSaldo = null;

                    for (Cliente c : BancoDeDadosFake.clientes) {

                        if (c.getEmail().equals(emailSaldo)) {

                            clienteSaldo = c;
                            break;
                        }
                    }

                    if (clienteSaldo == null) {

                        System.out.println("Cliente não encontrado.");

                        break;
                    }

                    System.out.print("Digite o valor: ");

                    double valor = scanner.nextDouble();

                    scanner.nextLine();

                    clienteSaldo.adicionarSaldo(valor);

                    System.out.println("Novo saldo: R$ " + clienteSaldo.getSaldo());

                    break;

                case 5:

                    System.out.println("\n=== RELATÓRIO CLIENTES ===");

                    for (Cliente c : BancoDeDadosFake.clientes) {

                        System.out.println(c.getNome() + " | Saldo: R$ " + c.getSaldo());
                    }

                    System.out.println("\n=== RELATÓRIO ESTAÇÕES ===");

                    for (EstacaoRecarga e : BancoDeDadosFake.estacoes) {

                        System.out.println(e);
                    }

                    break;

                case 6:

                    System.out.print("\nNome do cliente: ");

                    String nome = scanner.nextLine();

                    System.out.print("Email: ");

                    String novoEmail = scanner.nextLine();

                    System.out.print("Senha: ");

                    String senha = scanner.nextLine();

                    System.out.print("Saldo inicial: ");

                    double saldo = scanner.nextDouble();

                    scanner.nextLine();

                    Cliente novoCliente = new Cliente(nome, novoEmail, senha, saldo);

                    BancoDeDadosFake.clientes.add(novoCliente);

                    System.out.println("\nCliente cadastrado com sucesso!");

                    break;

                case 7:

                    System.out.print("\nDigite o email do cliente: ");

                    String emailCliente = scanner.nextLine();

                    Cliente clienteVeiculo = null;

                    for (Cliente c : BancoDeDadosFake.clientes) {

                        if (c.getEmail().equals(emailCliente)) {

                            clienteVeiculo = c;
                            break;
                        }
                    }

                    if (clienteVeiculo == null) {

                        System.out.println("Cliente não encontrado.");

                        break;
                    }

                    System.out.print("Modelo do veículo: ");

                    String modelo = scanner.nextLine();

                    System.out.print("Placa: ");

                    String placa = scanner.nextLine();

                    System.out.print("Capacidade máxima da bateria: ");

                    double bateria = scanner.nextDouble();

                    System.out.print("Bateria inicial atual: ");

                    double bateriaInicial = scanner.nextDouble();

                    scanner.nextLine();

                    System.out.println("\nTipos de conector:");

                    System.out.println("1 - CCS2");

                    System.out.println("2 - CHADEMO");

                    System.out.println("3 - TIPO2");

                    System.out.print("Escolha: ");

                    int tipoEscolhido = scanner.nextInt();

                    scanner.nextLine();

                    TipoConector conector;

                    switch (tipoEscolhido) {

                        case 1:

                            conector = TipoConector.CCS2;

                            break;

                        case 2:

                            conector = TipoConector.CHADEMO;

                            break;

                        default:

                            conector = TipoConector.TIPO2;
                    }

                    VeiculoEletrico novoVeiculo = new VeiculoEletrico(modelo, placa, bateria, conector);

                    novoVeiculo.carregar(bateriaInicial);

                    clienteVeiculo.cadastrarVeiculo(novoVeiculo);

                    System.out.println("\nVeículo cadastrado com sucesso!");

                    break;

                case 8:

                    System.out.print("\nID da estação: ");

                    int novoId = scanner.nextInt();

                    scanner.nextLine();

                    System.out.print("Localização: ");

                    String local = scanner.nextLine();

                    System.out.print("Potência (kW): ");

                    double potencia = scanner.nextDouble();

                    System.out.print("Energia inicial da estação: ");

                    double energiaInicial = scanner.nextDouble();

                    scanner.nextLine();

                    System.out.println("\nTipos de conector:");

                    System.out.println("1 - CCS2");

                    System.out.println("2 - CHADEMO");

                    System.out.println("3 - TIPO2");

                    System.out.print("Escolha: ");

                    int escolhaConector = scanner.nextInt();

                    scanner.nextLine();

                    TipoConector conectorEstacao;

                    switch (escolhaConector) {

                        case 1:

                            conectorEstacao = TipoConector.CCS2;

                            break;

                        case 2:

                            conectorEstacao = TipoConector.CHADEMO;

                            break;

                        default:

                            conectorEstacao = TipoConector.TIPO2;
                    }

                    EstacaoRecarga novaEstacao = new EstacaoRecarga(novoId, local, potencia, conectorEstacao, energiaInicial);

                    BancoDeDadosFake.estacoes.add(novaEstacao);

                    System.out.println("\nEstação cadastrada com sucesso!");

                    break;

                case 9:

                    System.out.print("\nDigite o email do cliente: ");

                    String emailDrive = scanner.nextLine();

                    Cliente clienteDrive = null;

                    for (Cliente c : BancoDeDadosFake.clientes) {

                        if (c.getEmail().equals(emailDrive)) {

                            clienteDrive = c;
                            break;
                        }
                    }

                    if (clienteDrive == null) {

                        System.out.println("Cliente não encontrado.");

                        break;
                    }

                    VeiculoEletrico carro = clienteDrive.getVeiculos().get(0);

                    System.out.print("Quantos km deseja dirigir? ");

                    double km = scanner.nextDouble();

                    scanner.nextLine();

                    carro.dirigir(km);

                    System.out.println("Bateria atual: " + carro.getBateriaAtual() + "/" + carro.getCapacidadeBateria() + " kWh");

                    break;

                case 10:

                    System.out.print("\nDigite o ID da estação: ");

                    int idEstacao = scanner.nextInt();

                    System.out.print("Energia adicionada: ");

                    double energia = scanner.nextDouble();

                    scanner.nextLine();

                    for (EstacaoRecarga e : BancoDeDadosFake.estacoes) {

                        if (e.getId() == idEstacao) {

                            e.recarregarEstacao(energia);

                            System.out.println("Energia atual: " + e.getEnergiaDisponivel() + " kWh");
                        }
                    }

                    break;

                case 11:

                    System.out.print("\nDigite o email do cliente: ");

                    String emailHistorico = scanner.nextLine();

                    Cliente clienteHistorico = null;

                    for (Cliente c : BancoDeDadosFake.clientes) {

                        if (c.getEmail().equals(emailHistorico)) {

                            clienteHistorico = c;
                            break;
                        }
                    }

                    if (clienteHistorico == null) {

                        System.out.println("Cliente não encontrado.");

                        break;
                    }

                    System.out.println("\n=== HISTÓRICO DE RECARGAS ===");

                    if (clienteHistorico.getHistoricoRecargas().isEmpty()) {

                        System.out.println("Nenhuma recarga encontrada.");

                    } else {

                        for (SessaoRecarga s : clienteHistorico.getHistoricoRecargas()) {

                            System.out.println("Veículo: " + s.getVeiculo().getModelo());

                            System.out.println("Energia: " + s.getEnergiaConsumida() + " kWh");

                            System.out.println("Valor: R$ " + s.getValorTotal());

                            System.out.println("-------------------");
                        }
                    }

                    break;

                case 12:

                    System.out.println("\n=== ESTAÇÕES ===");

                    for (EstacaoRecarga e : BancoDeDadosFake.estacoes) {

                        System.out.println(e);
                    }

                    System.out.print("\nDigite o ID da estação: ");

                    int idHistorico = scanner.nextInt();

                    scanner.nextLine();

                    EstacaoRecarga estacaoHistorico = null;

                    for (EstacaoRecarga e : BancoDeDadosFake.estacoes) {

                        if (e.getId() == idHistorico) {

                            estacaoHistorico = e;
                            break;
                        }
                    }

                    if (estacaoHistorico == null) {

                        System.out.println("Estação não encontrada.");

                        break;
                    }

                    System.out.println("\n=== HISTÓRICO DA ESTAÇÃO ===");

                    if (estacaoHistorico.getHistoricoVeiculos().isEmpty()) {

                        System.out.println("Nenhum veículo utilizou esta estação.");

                    } else {

                        for (String historico : estacaoHistorico.getHistoricoVeiculos()) {

                            System.out.println(historico);
                        }
                    }

                    break;

                case 0:

                    System.out.println("\nSistema encerrado.");

                    break;

                default:

                    System.out.println("\nOpção inválida.");
            }

        } while (opcao != 0);
    }
}