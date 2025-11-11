package biblioteca.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import biblioteca.exception.EntidadeNaoEncontradaException;
import biblioteca.exception.ValidacaoException;
import biblioteca.model.Livro;
import biblioteca.model.Livro.Status;

public class LivroRepositorioArquivo implements Repositorio<Livro> {

    private static final String FILE_PATH = "data/livros.csv";

    public LivroRepositorioArquivo() {
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
    private Livro deCsv(String linhaCsv) {
        String[] campos = linhaCsv.split(";");
        if (campos.length == 6) {
            int id = Integer.parseInt(campos[0]);
            String titulo = campos[1];
            String autor = campos[2];
            String isbn = campos[3];
            int ano = Integer.parseInt(campos[4]);
            Status status = Status.valueOf(campos[5]);
            
            return new Livro(id, titulo, autor, isbn, ano, status);
        }
        return null;
    }

    private String paraCsv(Livro livro) {
        return livro.getId() + ";" +
               livro.getTitulo() + ";" +
               livro.getAutor() + ";" +
               livro.getIsbn() + ";" +
               livro.getAno() + ";" +
               livro.getStatus().name() + "\n"; 
    }
    @Override
    public void adicionar(Livro livro) throws ValidacaoException {
        try {
            buscaPorId(livro.getId());
            throw new ValidacaoException("Já existe um livro com o ID " + livro.getId());
        } catch (EntidadeNaoEncontradaException e) {
            // ID não existe, pode adicionar
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(paraCsv(livro));
        } catch (IOException e) {
            System.err.println("Erro ao adicionar livro no arquivo: " + e.getMessage());
        }
    }

    @Override
    public Livro buscaPorId(int id) throws EntidadeNaoEncontradaException {
        List<Livro> todos = listaTodos();
        for (Livro l : todos) {
            if (l.getId() == id) {
                return l;
            }
        }
        throw new EntidadeNaoEncontradaException("Livro com ID " + id + " não encontrado.");
    }

    @Override
    public List<Livro> listaTodos() {
        List<Livro> livros = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Livro l = deCsv(linha);
                if (l != null) {
                    livros.add(l);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de livros: " + e.getMessage());
        }
        return livros;
    }

    @Override
    public void atualizar(Livro entidade) throws EntidadeNaoEncontradaException, ValidacaoException {
        List<Livro> livros = listaTodos();
        boolean encontrou = false;
        
        for (int i = 0; i < livros.size(); i++) {
            if (livros.get(i).getId() == entidade.getId()) {
                livros.set(i, entidade); 
                encontrou = true;
                break;
            }
        }

        if (!encontrou) {
            throw new EntidadeNaoEncontradaException("Livro com ID " + entidade.getId() + " não encontrado para atualizar.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Livro l : livros) {
                writer.write(paraCsv(l));
            }
        } catch (IOException e) {
            System.err.println("Erro ao atualizar o arquivo de livros: " + e.getMessage());
        }
    }

    @Override
    public void remover(int id) throws EntidadeNaoEncontradaException {
        List<Livro> livros = listaTodos();
        boolean removeu = livros.removeIf(l -> l.getId() == id);

        if (!removeu) {
            throw new EntidadeNaoEncontradaException("Livro com ID " + id + " não encontrado para remover.");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Livro l : livros) {
                writer.write(paraCsv(l));
            }
        } catch (IOException e) {
            System.err.println("Erro ao re-escrever o arquivo de livros: " + e.getMessage());
        }
    }
}