package biblioteca.model;

import biblioteca.model.EntidadeBase;

public class Livro implements EntidadeBase {

    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private int ano;
    private Status status;

    // Enum para status do livro
    public enum Status{
        DISPONIVEL, EMPRESTADO
    }

    public Livro(){}

    //Construtor
    public Livro(int id, String titulo, String autor, String isbn, int ano, Status status){
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.ano = ano;
        this.status = status;
    }

    @Override
    public String toString(){
        return "Livro{" +
            "id=" + id +
            ", titulo='" + titulo + '\'' +
            ", autor='" + autor + '\'' +
            ", isbn='" + isbn + '\'' +
            ", ano=" + ano +
            ", status=" + status +
        '}';
    }
    
    // Getters e Setters
    public int getId(){ 
        return id; 
    }
    public void setId(int id){ 
        this.id = id; 
    }
    public String getTitulo(){ 
        return titulo; 
    }
    public void setTitulo(String titulo){ 
        this.titulo = titulo; 
    }
    public String getAutor(){ 
        return autor; 
    }
    public void setAutor(String autor){ 
        this.autor = autor; 
    }
    public String getIsbn(){ 
        return isbn; 
    }
    public void setIsbn(String isbn){ 
        this.isbn = isbn; 
    }
    public int getAno(){ 
        return ano; 
    }
    public void setAno(int ano){ 
        this.ano = ano; 
    }
    public Status getStatus(){ 
        return status; 
    }
    public void setStatus(Status status){ 
        this.status = status; 
    }

}
