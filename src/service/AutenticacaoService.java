package service;

import model.Usuario;

import java.util.List;

public class AutenticacaoService {

    public Usuario autenticar(List<Usuario> usuarios, String email, String senha) {

        for (Usuario u : usuarios) {

            if (u.login(email, senha)) {

                System.out.println("Login realizado com sucesso.");

                return u;
            }
        }

        System.out.println("Email ou senha incorretos.");

        return null;
    }
}