package biblioteca.repository;

import java.util.List;
import biblioteca.exception.EntidadeNaoEncontradaException;
import biblioteca.exception.ValidacaoException;

public interface Repositorio<T> {
    
    void adicionar(T entidade) throws ValidacaoException;

    T buscaPorId(int id) throws EntidadeNaoEncontradaException;

    void atualizar(T entidade) throws EntidadeNaoEncontradaException, ValidacaoException;

    void remover(int id) throws EntidadeNaoEncontradaException;


    List<T> listaTodos();
}