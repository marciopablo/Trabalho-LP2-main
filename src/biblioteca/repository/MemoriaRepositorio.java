package biblioteca.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import biblioteca.exception.EntidadeNaoEncontradaException;
import biblioteca.exception.ValidacaoException;
import biblioteca.model.EntidadeBase;
public class MemoriaRepositorio<T extends EntidadeBase> implements Repositorio<T>{

    private Map<Integer, T> bancoDeDados = new HashMap<>();
    
    private AtomicInteger contadorId = new AtomicInteger(1);

    @Override
    public void adicionar(T entidade) throws ValidacaoException {
        int id = contadorId.getAndIncrement();
        
        entidade.setId(id);
        
        bancoDeDados.put(id, entidade);
    }

    @Override
    public T buscaPorId(int id) throws EntidadeNaoEncontradaException {
        T entidade = bancoDeDados.get(id);
        
        if (entidade == null) {
           throw new EntidadeNaoEncontradaException("Entidade com ID " + id + " não encontrada.");
        }
        return entidade;
    }


    @Override
    public void atualizar(T entidade) throws EntidadeNaoEncontradaException, ValidacaoException {
        int id = entidade.getId();

        buscaPorId(id); 
        
        bancoDeDados.put(id, entidade);
    }

    @Override
    public void remover(int id) throws EntidadeNaoEncontradaException {

        buscaPorId(id);
        
        bancoDeDados.remove(id);
    }

    @Override
    public List<T> listaTodos() {
        return new ArrayList<>(bancoDeDados.values());
    }
}