package biblioteca.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import biblioteca.exception.EntidadeNaoEncontradaException;
import biblioteca.exception.ValidacaoException;
import biblioteca.model.Categoria;

public class CategoriaRepositorioArquivo implements Repositorio<Categoria> {

    private static final String FILE_PATH = "data/categorias.csv";

    public CategoriaRepositorioArquivo() {
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

    private Categoria deCsv(String linhaCsv) {
        String[] campos = linhaCsv.split(";");
        if (campos.length == 3) {
            int id = Integer.parseInt(campos[0]);
            String nome = campos[1];
            String descricao = campos[2];
            return new Categoria(id, nome, descricao);
        }
        return null;
    }

    private String paraCsv(Categoria categoria) {
        return categoria.getId() + ";" +
               categoria.getNome() + ";" +
               categoria.getDescricao() + "\n";
    }

    @Override
    public void adicionar(Categoria categoria) throws ValidacaoException {

        try {
            buscaPorId(categoria.getId());
            throw new ValidacaoException("Já existe uma categoria com o ID " + categoria.getId());
        } catch (EntidadeNaoEncontradaException e) {
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(paraCsv(categoria));
        } catch (IOException e) {
            System.err.println("Erro ao adicionar categoria no arquivo: " + e.getMessage());
        }
    }

    @Override
    public Categoria buscaPorId(int id) throws EntidadeNaoEncontradaException {
        List<Categoria> todas = listaTodos();
        for (Categoria c : todas) {
            if (c.getId() == id) {
                return c;
            }
        }
        throw new EntidadeNaoEncontradaException("Categoria com ID " + id + " não encontrada.");
    }

    @Override
    public List<Categoria> listaTodos() {
        List<Categoria> categorias = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                Categoria c = deCsv(linha);
                if (c != null) {
                    categorias.add(c);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de categorias: " + e.getMessage());
        }
        return categorias;
    }

    @Override
    public void atualizar(Categoria entidade) throws EntidadeNaoEncontradaException, ValidacaoException {
        List<Categoria> categorias = listaTodos();
        boolean encontrou = false;
        
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getId() == entidade.getId()) {
                categorias.set(i, entidade); // Substitui o antigo pelo novo
                encontrou = true;
                break;
            }
        }

        if (!encontrou) {
            throw new EntidadeNaoEncontradaException("Categoria com ID " + entidade.getId() + " não encontrada para atualizar.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Categoria c : categorias) {
                writer.write(paraCsv(c));
            }
        } catch (IOException e) {
            System.err.println("Erro ao atualizar o arquivo de categorias: " + e.getMessage());
        }
    }

    @Override
    public void remover(int id) throws EntidadeNaoEncontradaException {
        List<Categoria> categorias = listaTodos();
        
        boolean removeu = categorias.removeIf(c -> c.getId() == id);

        if (!removeu) {
            throw new EntidadeNaoEncontradaException("Categoria com ID " + id + " não encontrada para remover.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Categoria c : categorias) {
                writer.write(paraCsv(c));
            }
        } catch (IOException e) {
            System.err.println("Erro ao re-escrever o arquivo de categorias: " + e.getMessage());
        }
    }
}