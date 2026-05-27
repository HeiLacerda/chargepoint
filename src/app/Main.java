package app;

import enums.TipoPagamento;

import exceptions.UsuarioNaoEncontradoException;

import model.*;

import service.AutenticacaoService;
import service.SistemaRecarga;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        SistemaRecarga sistema = new SistemaRecarga();

        Cliente cliente = new Cliente("Heitor", "heitor@gmail.com", "123", 500);

        cliente.exibirMenu();

        cliente.adicionarSaldo(100);

        VeiculoEletrico carro = new VeiculoEletrico("BYD Dolphin", "ABC-1234", 44);

        cliente.cadastrarVeiculo(carro);

        EstacaoRecarga estacao = new EstacaoRecarga(1, "Shopping Cariri", 150);

        sistema.cadastrarCliente(cliente);

        sistema.cadastrarEstacao(estacao);

        sistema.listarEstacoes();

        Administrador admin = new Administrador("Carlos", "admin@gmail.com", "admin123");

        admin.exibirMenu();

        admin.gerarRelatorio();

        List<Usuario> usuarios = new ArrayList<>();

        usuarios.add(cliente);

        usuarios.add(admin);

        AutenticacaoService auth = new AutenticacaoService();

        try {

            Usuario usuarioLogado = auth.autenticar(usuarios, "heitor@gmail.com", "123");

            if (usuarioLogado == null) {

                throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
            }

            System.out.println("Usuário autenticado: " + usuarioLogado.getNome());

            SessaoRecarga sessao = new SessaoRecarga(cliente, carro, estacao);

            sessao.iniciarRecarga(20);

            Pagamento pagamento = new Pagamento(TipoPagamento.PIX);

            pagamento.realizarPagamento(sessao.getValorTotal());

            cliente.descontarSaldo(sessao.getValorTotal());

            sessao.encerrarRecarga();

        } catch (Exception e) {

            System.out.println("ERRO: " + e.getMessage());
        }

        RelatorioSistema relatorio = new RelatorioSistema();

        relatorio.gerarRelatorioEstacoes(sistema.getEstacoes());

        relatorio.gerarRelatorioClientes(sistema.getClientes());
    }
}