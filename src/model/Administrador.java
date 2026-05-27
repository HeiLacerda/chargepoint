package model;

public class Administrador extends Usuario {

    public Administrador(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    @Override
    public void exibirMenu() {
        System.out.println("=== MENU ADMINISTRADOR ===");
    }

    public void gerarRelatorio() {
        System.out.println("Relatório gerado com sucesso.");
    }
}