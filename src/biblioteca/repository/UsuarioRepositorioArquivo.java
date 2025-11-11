package biblioteca.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import biblioteca.exception.EntidadeNaoEncontradaException;
import biblioteca.exception.ValidacaoException;
import biblioteca.model.Usuario; 
import biblioteca.model.Aluno; 
import biblioteca.model.Professor;
import biblioteca.model.Funcionario;

public class UsuarioRepositorioArquivo implements Repositorio<Usuario> {

    private static final String FILE_PATH = "data/usuarios.csv";

    public UsuarioRepositorioArquivo() {
        criarArquivoSeNaoExistir();
    }

    private void criarArquivoSeNaoExistir() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo: " + e.getMessage());
        }
    }

    private Usuario deCsv(String linhaCsv) {
        String[] campos = linhaCsv.split(";");
        
        String tipo = campos[0];
        
        int id = Integer.parseInt(campos[1]);
        String nome = campos[2];
        String email = campos[3];
        String telefone = campos[4];
        String campoEspecifico = campos[5];

        switch (tipo) {
            case "ALUNO":
                return new Aluno(id, nome, email, telefone, campoEspecifico); 
            case "PROFESSOR":
                return new Professor(id, nome, email, telefone, campoEspecifico);
            case "FUNCIONARIO":
                return new Funcionario(id, nome, email, telefone, campoEspecifico);
            default:
                System.err.println("Tipo de usuário desconhecido no CSV: " + tipo);
                return null;
        }
    }

    
    private String paraCsv(Usuario usuario) {
        String baseCsv = usuario.getId() + ";" +
                         usuario.getNome() + ";" +
                         usuario.getEmail() + ";" +
                         usuario.getTelefone();

        if (usuario instanceof Aluno) {
            Aluno a = (Aluno) usuario;
            return "ALUNO;" + baseCsv + ";" + a.getMatricula() + "\n";
            
        } else if (usuario instanceof Professor) {
            Professor p = (Professor) usuario;
            return "PROFESSOR;" + baseCsv + ";" + p.getSiape() + "\n";
            
        } else if (usuario instanceof Funcionario) {
            Funcionario f = (Funcionario) usuario;
            return "FUNCIONARIO;" + baseCsv + ";" + f.getCargo() + "\n";
        }
        return "";
    }
    @Override
    public void adicionar(Usuario usuario) throws ValidacaoException {
        try {
            buscaPorId(usuario.getId());
            throw new ValidacaoException("Já existe um usuário com o ID " + usuario.getId());
        } catch (EntidadeNaoEncontradaException e) {
            // ID não existe, pode adicionar
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(paraCsv(usuario));
        } catch (IOException e) {
            System.err.println("Erro ao adicionar usuário no arquivo: " + e.getMessage());
        }
    }

    @Override
    public Usuario buscaPorId(int id) throws EntidadeNaoEncontradaException {
        List<Usuario> todos = listaTodos();
        for (Usuario u : todos) {
            if (u.getId() == id) {
                return u;
            }
        }
        throw new EntidadeNaoEncontradaException("Usuário com ID " + id + " não encontrado.");
    }

    @Override
    public List<Usuario> listaTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Usuario u = deCsv(linha);
                if (u != null) {
                    usuarios.add(u);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de usuários: " + e.getMessage());
        }
        return usuarios;
    }

    @Override
    public void atualizar(Usuario entidade) throws EntidadeNaoEncontradaException, ValidacaoException {
        List<Usuario> usuarios = listaTodos();
        boolean encontrou = false;
        
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == entidade.getId()) {
                usuarios.set(i, entidade);
                encontrou = true;
                break;
            }
        }

        if (!encontrou) {
            throw new EntidadeNaoEncontradaException("Usuário com ID " + entidade.getId() + " não encontrado para atualizar.");
        }

        // Re-escreve o arquivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Usuario u : usuarios) {
                writer.write(paraCsv(u));
            }
        } catch (IOException e) {
            System.err.println("Erro ao atualizar o arquivo de usuários: " + e.getMessage());
        }
    }

    @Override
    public void remover(int id) throws EntidadeNaoEncontradaException {
        List<Usuario> usuarios = listaTodos();
        boolean removeu = usuarios.removeIf(u -> u.getId() == id);

        if (!removeu) {
            throw new EntidadeNaoEncontradaException("Usuário com ID " + id + " não encontrado para remover.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Usuario u : usuarios) {
                writer.write(paraCsv(u));
            }
        } catch (IOException e) {
            System.err.println("Erro ao re-escrever o arquivo de usuários: " + e.getMessage());
        }
    }
}